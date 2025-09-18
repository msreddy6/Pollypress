package com.example.pollypress.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import org.json.JSONObject;
import org.json.JSONException;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.pollypress.R;
import com.example.pollypress.VolleySingleton;

public class SignupActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Spinner roleSpinner;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        signupButton = findViewById(R.id.signupButton);

        String[] roles = {"newsuser", "reporter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        signupButton.setOnClickListener(v -> performSignup());
    }

    private void performSignup() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String endpoint = role.equals("newsuser") ? "newsusers" : "reporters";
        String url = BASE_URL + endpoint;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errMsg = "Signup failed";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errMsg += ": " + new String(error.networkResponse.data);
                    }
                    Toast.makeText(SignupActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
