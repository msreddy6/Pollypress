package com.example.pollypress.content;

import android.os.Bundle;
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

public class EditArticleActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private EditText etTitle, etContent, etAuthor, etPublication, etDate;
    private Button btnUpdate, btnDelete;
    private int articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        etTitle = findViewById(R.id.etEditTitle);
        etContent = findViewById(R.id.etEditContent);
        etAuthor = findViewById(R.id.etEditAuthor);
        etPublication = findViewById(R.id.etEditPublication);
        etDate = findViewById(R.id.etEditDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Retrieve passed article details from intent extras
        articleId = getIntent().getIntExtra("articleId", -1);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String author = getIntent().getStringExtra("author");
        String publication = getIntent().getStringExtra("publication");
        String publicationDate = getIntent().getStringExtra("publicationDate");

        etTitle.setText(title);
        etContent.setText(content);
        etAuthor.setText(author);
        etPublication.setText(publication);
        etDate.setText(publicationDate);

        btnUpdate.setOnClickListener(v -> updateArticle());
        btnDelete.setOnClickListener(v -> deleteArticle());
    }

    private void updateArticle() {
        String updatedTitle = etTitle.getText().toString().trim();
        String updatedContent = etContent.getText().toString().trim();
        String updatedAuthor = etAuthor.getText().toString().trim();
        String updatedPublication = etPublication.getText().toString().trim();
        String updatedDate = etDate.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedContent.isEmpty() || updatedAuthor.isEmpty() ||
                updatedPublication.isEmpty() || updatedDate.isEmpty()) {
            Toast.makeText(EditArticleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "articles/" + articleId;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", updatedTitle);
            jsonBody.put("content", updatedContent);
            jsonBody.put("author", updatedAuthor);
            jsonBody.put("publication", updatedPublication);
            jsonBody.put("publicationDate", updatedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    Toast.makeText(EditArticleActivity.this, "Article updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> Toast.makeText(EditArticleActivity.this, "Failed to update article", Toast.LENGTH_SHORT).show());

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deleteArticle() {
        String markDeleteUrl = BASE_URL + "articles/" + articleId;

        StringRequest markRequest = new StringRequest(Request.Method.DELETE, markDeleteUrl,
                response -> {
                    String finalizeUrl = BASE_URL + "articles/" + articleId + "/finalizeDeletion";
                    StringRequest finalizeRequest = new StringRequest(Request.Method.DELETE, finalizeUrl,
                            finalizeResponse -> {
                                Toast.makeText(EditArticleActivity.this, "Article deleted successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity after deletion
                            },
                            finalizeError -> {
                                Toast.makeText(EditArticleActivity.this, "Finalize deletion failed", Toast.LENGTH_SHORT).show();
                                finalizeError.printStackTrace();
                            });

                    VolleySingleton.getInstance(EditArticleActivity.this).addToRequestQueue(finalizeRequest);
                },
                error -> {
                    Toast.makeText(EditArticleActivity.this, "Failed to mark article for deletion", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(markRequest);
    }

}
