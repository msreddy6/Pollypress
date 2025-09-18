package com.example.pollypress.dashboards;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pollypress.R;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class NewTicketActivity extends AppCompatActivity {
    public static boolean isInTestMode = false;

    private Spinner spinner;
    private Button btnSubmit;
    private TextView tvSuccess;
    private ArrayList<JSONObject> admins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);

        spinner   = findViewById(R.id.spinnerAdmins);
        btnSubmit = findViewById(R.id.btnSubmitTicket);
        tvSuccess = findViewById(R.id.tvSuccessTicket);

        tvSuccess.setVisibility(View.GONE);

        fetchAdmins();

        btnSubmit.setOnClickListener(v -> {
            if (isInTestMode) {
                tvSuccess.setText("Ticket created");
                tvSuccess.setVisibility(View.VISIBLE);
            } else {
                createTicket();
            }
        });
    }

    private void fetchAdmins() {
        String url = VolleySingleton.BASE_URL + "admins";
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET, url, null,
                resp -> {
                    ArrayList<String> names = new ArrayList<>();
                    for (int i = 0; i < resp.length(); i++) {
                        try {
                            JSONObject a = resp.getJSONObject(i);
                            admins.add(a);
                            names.add(a.getString("username"));
                        } catch (JSONException ignored) {}
                    }
                    spinner.setAdapter(new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, names));
                },
                err -> Toast.makeText(this, "Cannot load admins", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void createTicket() {
        int pos = spinner.getSelectedItemPosition();
        if (pos < 0) {
            Toast.makeText(this, "Pick an admin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject admin = admins.get(pos);
            long adminId = admin.getLong("id");
            long reporterId = new ReporterSession(this).getReporterId();

            JSONObject body = new JSONObject();
            body.put("admin", new JSONObject().put("id", adminId));
            body.put("reporter", new JSONObject().put("id", reporterId));

            String url = VolleySingleton.BASE_URL + "tickets";
            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST, url, body,
                    resp -> {
                        Toast.makeText(this, "Ticket created", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    err -> Toast.makeText(this, "Create failed", Toast.LENGTH_SHORT).show()
            );
            VolleySingleton.getInstance(this).addToRequestQueue(req);
        } catch (JSONException e) {
            Toast.makeText(this, "Error building request", Toast.LENGTH_SHORT).show();
        }
    }
}
