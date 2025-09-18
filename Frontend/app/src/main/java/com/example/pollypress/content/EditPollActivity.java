package com.example.pollypress.content;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pollypress.R;
import com.example.pollypress.VolleySingleton;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditPollActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private static final String TAG = "EditPollActivity";
    private EditText etTitle, etDescription, etOptions;
    private Button btnUpdate, btnDelete;
    private long pollId; // Changed to long

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poll);

        etTitle = findViewById(R.id.etEditPollTitle);
        etDescription = findViewById(R.id.etEditPollDescription);
        etOptions = findViewById(R.id.etEditPollOptions);
        btnUpdate = findViewById(R.id.btnUpdatePoll);
        btnDelete = findViewById(R.id.btnDeletePoll);

        // Retrieve pollId as a long extra
        pollId = getIntent().getLongExtra("pollId", -1);
        if (pollId == -1) {
            Toast.makeText(this, "Invalid poll ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String optionsStr = getIntent().getStringExtra("options");

        etTitle.setText(title);
        etDescription.setText(description);
        String cleanedOptions = optionsStr.replaceAll("[\\[\\]]", "");
        etOptions.setText(cleanedOptions);

        btnUpdate.setOnClickListener(v -> updatePoll());
        btnDelete.setOnClickListener(v -> deletePoll());
    }

    private void updatePoll() {
        String updatedTitle = etTitle.getText().toString().trim();
        String updatedDescription = etDescription.getText().toString().trim();
        String updatedOptionsStr = etOptions.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || updatedOptionsStr.isEmpty()){
            Toast.makeText(EditPollActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> options = new ArrayList<>(Arrays.asList(updatedOptionsStr.split(",")));
        for (int i = 0; i < options.size(); i++){
            options.set(i, options.get(i).trim());
        }

        String url = BASE_URL + "polls/" + pollId;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", updatedTitle);
            jsonBody.put("description", updatedDescription);
            jsonBody.put("options", new JSONArray(options));
        } catch(JSONException e) {
            e.printStackTrace();
            Toast.makeText(EditPollActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    Toast.makeText(EditPollActivity.this, "Poll updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errMsg = "Failed to update poll";
                    if (error.networkResponse != null) {
                        errMsg += " (HTTP " + error.networkResponse.statusCode + "): " +
                                new String(error.networkResponse.data);
                    } else {
                        errMsg += " " + error.getMessage();
                    }
                    Log.e(TAG, errMsg);
                    Toast.makeText(EditPollActivity.this, errMsg, Toast.LENGTH_LONG).show();
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deletePoll() {
        String url = BASE_URL + "polls/" + pollId;
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(EditPollActivity.this, "Poll deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errMsg = "Failed to delete poll";
                    if (error.networkResponse != null) {
                        errMsg += " (HTTP " + error.networkResponse.statusCode + "): " +
                                new String(error.networkResponse.data);
                    } else {
                        errMsg += " " + error.getMessage();
                    }
                    Log.e(TAG, errMsg);
                    Toast.makeText(EditPollActivity.this, errMsg, Toast.LENGTH_LONG).show();
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
