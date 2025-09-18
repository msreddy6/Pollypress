package com.example.pollypress.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pollypress.R;
import com.example.pollypress.adapter.TicketAdapter;
import com.example.pollypress.model.TicketModel;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.VolleySingleton;

import java.util.ArrayList;

public class ReporterTicketsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TicketAdapter adapter;
    private ArrayList<TicketModel> list = new ArrayList<>();
    private ReporterSession session;
    private Button btnNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter_tickets);

        session = new ReporterSession(this);
        rv  = findViewById(R.id.rvReporterTickets);
        btnNew = findViewById(R.id.btnNewTicket);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketAdapter(list, ticket ->
                startActivity(new Intent(this, TicketChatActivity.class)
                        .putExtra("ticketId", ticket.getId())
                )
        );
        rv.setAdapter(adapter);

        btnNew.setOnClickListener(v ->
                startActivity(new Intent(this, NewTicketActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTickets();
    }

    private void fetchTickets() {
        long reporterId = session.getReporterId();
        String url = VolleySingleton.BASE_URL + "tickets/reporter/" + reporterId;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                resp -> {
                    list.clear();
                    for (int i = 0; i < resp.length(); i++) try {
                        org.json.JSONObject obj = resp.getJSONObject(i);
                        TicketModel t = new TicketModel();
                        t.setId(obj.getLong("id"));
                        t.setResolved(obj.optBoolean("resolved"));
                        t.setCreatedAt(obj.optString("createdAt"));
                        t.setResolvedAt(obj.optString("resolvedAt"));
                        // nested objects
                        t.setReporterUsername(obj.getJSONObject("reporter").getString("username"));
                        t.setAdminUsername(obj.getJSONObject("admin").getString("username"));
                        list.add(t);
                    } catch (Exception e) { }
                    adapter.notifyDataSetChanged();
                },
                err -> Toast.makeText(this, "Cannot load tickets", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }
}
