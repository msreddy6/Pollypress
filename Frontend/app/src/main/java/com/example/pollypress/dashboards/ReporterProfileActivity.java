package com.example.pollypress.dashboards;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.R;
import com.example.pollypress.VolleySingleton;
import com.example.pollypress.auth.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class ReporterProfileActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private ImageView profileImage;
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporter_profile);

        ReporterSession session = new ReporterSession(this);
        int reporterId = session.getReporterId();
        String username = session.getReporterUsername();
        String email    = session.getReporterEmail();

        tvGreeting   = findViewById(R.id.tvGreeting);
        profileImage = findViewById(R.id.profileImage);
        tvGreeting.setText("Hi, " + username + "\n" + email);

        Button btnEdit    = findViewById(R.id.btnEditProfile);
        Button btnShare   = findViewById(R.id.btnShareApp);
        Button btnLogout  = findViewById(R.id.btnLogout);
        Button btnDelete  = findViewById(R.id.btnDeleteAccount);
        Button btnBack    = findViewById(R.id.btnBackHome);

        btnEdit.setOnClickListener(v -> showEditDialog(reporterId, session));
        btnLogout.setOnClickListener(v -> {
            session.clearSession();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Check out PollyPress News App!");
            startActivity(Intent.createChooser(share, "Share via"));
        });
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> confirmDelete(reporterId));

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        profileImage.setImageURI(uri);
                    }
                });
        profileImage.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK);
            pick.setType("image/*");
            imagePickerLauncher.launch(pick);
        });
    }

    private void showEditDialog(int id, ReporterSession session) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.dialog_edit_profile, null);
        b.setView(view);
        AlertDialog d = b.create();

        EditText etUser = view.findViewById(R.id.etDialogUsername);
        EditText etPass = view.findViewById(R.id.etDialogPassword);
        Button   btnSave = view.findViewById(R.id.btnSaveChanges);

        btnSave.setOnClickListener(v -> {
            String newU = etUser.getText().toString().trim();
            String newP = etPass.getText().toString().trim();
            updateReporter(id,
                    newU.isEmpty() ? session.getReporterUsername() : newU,
                    newP.isEmpty() ? session.getReporterPassword() : newP,
                    session);
            d.dismiss();
        });
        d.show();
    }

    private void updateReporter(int id, String username, String password, ReporterSession session) {
        String url = BASE_URL + "reporters/" + id;

        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("email",    session.getReporterEmail());
            body.put("password", password);
        } catch (JSONException e) {
            Log.e("ReporterProfile", "JSON error", e);
        }

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                response -> {
                    session.saveReporterDetails(id, username, session.getReporterEmail(), password);
                    tvGreeting.setText("Hi, " + username + "\n" + session.getReporterEmail());
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    String bodyText = "";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        bodyText = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    }
                    Log.e("ReporterProfile", "Update failed: " + bodyText, error);
                    Toast.makeText(this, "Update failed: " + bodyText, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        VolleySingleton.getInstance(this)
                .addToRequestQueue(req);
    }

    private void confirmDelete(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("This cannot be undone.")
                .setPositiveButton("Yes", (d,w) -> deleteReporter(id))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteReporter(int id) {
        String url = BASE_URL + "reporters/" + id;
        StringRequest req = new StringRequest(Request.Method.DELETE, url,
                r -> {
                    new ReporterSession(this).clearSession();
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                },
                e -> Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this)
                .addToRequestQueue(req);
    }
}
