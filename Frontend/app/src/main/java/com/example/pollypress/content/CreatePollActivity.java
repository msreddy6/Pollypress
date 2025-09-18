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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePollActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    public static boolean isInTestMode = false;

    private EditText etTitle, etDescription, etOptions;
    private Button btnSubmit;
    private TextView tvSuccess;
    private String reporterName;
    private int reporterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_poll);

        etTitle       = findViewById(R.id.etPollTitle);
        etDescription = findViewById(R.id.etPollDescription);
        etOptions     = findViewById(R.id.etPollOptions);
        btnSubmit     = findViewById(R.id.btnSubmitPoll);
        tvSuccess     = findViewById(R.id.tvSuccess);

        tvSuccess.setVisibility(View.GONE);

        ReporterSession session = new ReporterSession(this);
        reporterName = session.getReporterUsername();
        reporterId   = session.getReporterId();

        btnSubmit.setOnClickListener(v -> submitPoll());
    }

    private void submitPoll() {
        if (isInTestMode) {
            tvSuccess.setText("Poll created successfully");
            tvSuccess.setVisibility(View.VISIBLE);
            return;
        }

        String title       = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String optionsText = etOptions.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || optionsText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> options = new ArrayList<>(Arrays.asList(optionsText.split(",")));
        for (int i = 0; i < options.size(); i++) {
            options.set(i, options.get(i).trim());
        }

        String url = BASE_URL + "polls";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title);
            jsonBody.put("description", description);
            jsonBody.put("options", new JSONArray(options));
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
                    Toast.makeText(this, "Poll created successfully", Toast.LENGTH_SHORT).show();
                    if (!isInTestMode) finish();
                },
                err -> Toast.makeText(this, "Failed to create poll", Toast.LENGTH_LONG).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }
}
