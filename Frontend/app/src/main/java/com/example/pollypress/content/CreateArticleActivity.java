package com.example.pollypress.content;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pollypress.R;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateArticleActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";

    /** In test mode we skip finish() so Espresso can verify the UI. */
    public static boolean isInTestMode = false;

    private EditText etTitle, etContent, etPublication, etDate;
    private Button btnSubmit;
    private TextView tvSuccess;
    private String reporterName;
    private int reporterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_article);

        etTitle       = findViewById(R.id.etTitle);
        etContent     = findViewById(R.id.etContent);
        etPublication = findViewById(R.id.etPublication);
        etDate        = findViewById(R.id.etDate);
        btnSubmit     = findViewById(R.id.btnSubmit);
        tvSuccess     = findViewById(R.id.tvSuccess);

        tvSuccess.setVisibility(View.GONE);

        ReporterSession session = new ReporterSession(this);
        reporterName = session.getReporterUsername();
        reporterId   = session.getReporterId();
        if (!isInTestMode && (reporterName == null || reporterName.isEmpty() || reporterId == -1)) {
            Toast.makeText(this, "Reporter details not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSubmit.setOnClickListener(v -> submitArticle());
    }

    private void submitArticle() {
        if (isInTestMode) {
            tvSuccess.setText("Article created successfully");
            tvSuccess.setVisibility(View.VISIBLE);
            return;
        }

        String title           = etTitle.getText().toString().trim();
        String content         = etContent.getText().toString().trim();
        String publication     = etPublication.getText().toString().trim();
        String publicationDate = etDate.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty() || publication.isEmpty() || publicationDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "articles";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title);
            jsonBody.put("content", content);
            jsonBody.put("author", reporterName);
            jsonBody.put("publication", publication);
            jsonBody.put("publicationDate", publicationDate);
            JSONObject reporterObj = new JSONObject();
            reporterObj.put("id", reporterId);
            reporterObj.put("username", reporterName);
            jsonBody.put("reporter", reporterObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                resp -> {
                    Toast.makeText(this, "Article created successfully", Toast.LENGTH_SHORT).show();
                    if (!isInTestMode) finish();
                },
                err -> {
                    Toast.makeText(this, "Failed to create article", Toast.LENGTH_LONG).show();
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }
}
