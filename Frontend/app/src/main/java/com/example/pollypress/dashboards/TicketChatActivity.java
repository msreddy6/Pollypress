package com.example.pollypress.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pollypress.AdminSession;
import com.example.pollypress.R;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.VolleySingleton;
import com.example.pollypress.adapter.MessageAdapter;
import com.example.pollypress.model.TicketMessageModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * TicketChatActivity manages the live chat interface for a specific support ticket.
 * It loads message history via REST, opens a WebSocket for real-time messaging,
 * and allows the user (reporter or admin) to send and receive chat messages.
 * @author Shivadhar Reddy Maddi
 */
public class TicketChatActivity extends AppCompatActivity {
    private static final String TAG = "TicketChat";

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnSend;
    private MessageAdapter adapter;
    private ArrayList<TicketMessageModel> msgs = new ArrayList<>();

    private OkHttpClient wsClient;
    private WebSocket webSocket;
    private long ticketId;
    private String myUser;

    /**
     * Called when the activity is first created.
     * Initializes UI components, loads message history, and opens the WebSocket connection.
     *
     * @param savedInstanceState The bundle containing the activity’s previously saved state, or null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);

        // Retrieve ticketId and determine current user (reporter or admin)
        ticketId = getIntent().getLongExtra("ticketId", -1);
        ReporterSession r = new ReporterSession(this);
        AdminSession    a = new AdminSession(this);
        myUser = (r.getReporterUsername() != null)
                ? r.getReporterUsername()
                : a.getAdminUsername();

        // Wire up UI components
        rvMessages = findViewById(R.id.rvMessages);
        etMessage  = findViewById(R.id.etMessage);
        btnSend    = findViewById(R.id.btnSend);

        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(msgs);
        rvMessages.setAdapter(adapter);

        // Load existing chat history and establish WebSocket
        loadHistory();
        setupWebSocketClient();
        openSocket();

        // Send button listener: send text over WebSocket if non-empty
        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty() && webSocket != null) {
                webSocket.send(text);
                etMessage.setText("");
            }
        });
    }

    /**
     * Called when the activity is destroyed.
     * Closes the WebSocket connection to free resources.
     */
    @Override
    protected void onDestroy() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye");
        }
        super.onDestroy();
    }

    /**
     * Fetches the chat history for this ticket via a REST call
     * and populates the RecyclerView with received messages.
     */
    private void loadHistory() {
        String url = VolleySingleton.BASE_URL + "tickets/" + ticketId + "/messages";
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    msgs.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject o = response.getJSONObject(i);
                            TicketMessageModel m = new TicketMessageModel();
                            m.setId(o.getLong("id"));
                            m.setSender(o.getString("sender"));
                            m.setMessage(o.getString("message"));
                            m.setSentAt(o.getString("sentAt"));
                            msgs.add(m);
                        } catch (JSONException ignored) { }
                    }
                    adapter.notifyDataSetChanged();
                    rvMessages.scrollToPosition(msgs.size() - 1);
                },
                error -> Toast.makeText(this, "Cannot load chat history", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Configures the OkHttpClient with logging for the WebSocket connection.
     * Sets up an HTTP logging interceptor at BODY level.
     */
    private void setupWebSocketClient() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor(msg ->
                Log.d(TAG, "WS log: " + msg)
        );
        log.setLevel(HttpLoggingInterceptor.Level.BODY);
        wsClient = new OkHttpClient.Builder()
                .addInterceptor(log)
                .build();
    }

    /**
     * Opens the WebSocket connection to the ticket-specific endpoint.
     * Handles onOpen, onMessage, and onFailure callbacks to update the UI.
     */
    private void openSocket() {
        String wsUrl = VolleySingleton.BASE_WS_URL
                + "ticket/" + ticketId + "/" + myUser;
        Log.d(TAG, "Connecting to WS: " + wsUrl);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = wsClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket ws, Response resp) {
                runOnUiThread(() ->
                        Toast.makeText(TicketChatActivity.this, "CONNECTED", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                runOnUiThread(() -> {
                    String[] parts = text.split(": ", 2);
                    TicketMessageModel m = new TicketMessageModel();
                    m.setSender(parts[0]);
                    m.setMessage(parts.length > 1 ? parts[1] : "");
                    m.setSentAt("");
                    msgs.add(m);
                    adapter.notifyItemInserted(msgs.size() - 1);
                    rvMessages.scrollToPosition(msgs.size() - 1);
                });
            }

            @Override
            public void onFailure(WebSocket ws, Throwable t, Response resp) {
                Log.e(TAG, "WS error", t);
                runOnUiThread(() ->
                        Toast.makeText(TicketChatActivity.this,
                                "WebSocket error: " + t.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
            }
        });
    }
}
