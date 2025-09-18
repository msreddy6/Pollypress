package com.example.pollypress.dashboards;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pollypress.R;
import com.example.pollypress.adapter.TicketAdapter;
import com.example.pollypress.model.TicketModel;
import com.example.pollypress.AdminSession;
import com.example.pollypress.VolleySingleton;
import org.json.JSONException;
import java.util.ArrayList;

public class AdminTicketsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TicketAdapter adapter;
    private ArrayList<TicketModel> list = new ArrayList<>();
    private AdminSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tickets);

        session = new AdminSession(this);
        rv = findViewById(R.id.rvAdminTickets);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketAdapter(list, this::onTicketClick);
        rv.setAdapter(adapter);
    }

    @Override protected void onResume() {
        super.onResume();
        fetchTickets();
    }

    private void fetchTickets() {
        long adminId = session.getAdminId();
        String url = VolleySingleton.BASE_URL + "tickets/admin/" + adminId;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                resp -> {
                    list.clear();
                    
                    for (int i=0;i<resp.length();i++) try {
                        org.json.JSONObject obj = resp.getJSONObject(i);
                        TicketModel t = new TicketModel();
                        t.setId(obj.getLong("id"));
                        t.setResolved(obj.optBoolean("resolved"));
                        t.setCreatedAt(obj.optString("createdAt"));
                        t.setResolvedAt(obj.optString("resolvedAt"));
                        t.setReporterUsername(obj.getJSONObject("reporter").getString("username"));
                        t.setAdminUsername(obj.getJSONObject("admin").getString("username"));
                        list.add(t);
                    } catch (JSONException e) { }
                    adapter.notifyDataSetChanged();
                },
                err -> Toast.makeText(this,"Load failed",Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void onTicketClick(TicketModel t) {
        if (t.isResolved()) {
            // just open chat
            startActivity(new Intent(this, TicketChatActivity.class)
                    .putExtra("ticketId", t.getId()));
        } else {
            // prompt to resolve or chat
            new AlertDialog.Builder(this)
                    .setTitle("Ticket #" + t.getId())
                    .setItems(new String[]{"Chat","Resolve"}, (d, which) -> {
                        if (which==0) {
                            startActivity(new Intent(this, TicketChatActivity.class)
                                    .putExtra("ticketId",t.getId()));
                        } else {
                            resolveTicket(t.getId());
                        }
                    }).show();
        }
    }

    private void resolveTicket(long id) {
        String url = VolleySingleton.BASE_URL + "tickets/" + id + "/resolve?adminId="
                + session.getAdminId();
        StringRequest req = new StringRequest(Request.Method.PUT, url,
                resp -> {
                    Toast.makeText(this,"Resolved",Toast.LENGTH_SHORT).show();
                    fetchTickets();
                },
                err -> Toast.makeText(this,"Resolve failed",Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }
}
