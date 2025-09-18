// ForgotPasswordActivity.java
package com.example.pollypress.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.example.pollypress.R;
import com.example.pollypress.VolleySingleton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPassword";
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private EditText emailEditText;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.emailEditText);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        forgotPasswordButton.setOnClickListener(v -> performForgotPassword());
    }

    private void performForgotPassword() {
        final String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "newsusers/forgotPassword";

        // Build a POST StringRequest with form parameters
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 200 OK from server → reset email sent
                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Password reset email sent!",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = "";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        }
                        Log.e(TAG, "ForgotPassword error: " + body, error);
                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Error: " + (body.isEmpty() ? "Request failed" : body),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // form fields
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                // if your API needs any headers (e.g. Accept), add them here
                Map<String,String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Enqueue
        VolleySingleton.getInstance(this)
                .addToRequestQueue(request);
    }
}
