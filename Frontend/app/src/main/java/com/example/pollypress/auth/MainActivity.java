package com.example.pollypress.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pollypress.AdminSession;
import com.example.pollypress.dashboards.NewsUserDashboardActivity;
import com.example.pollypress.NewsUserSession;
import com.example.pollypress.R;
import com.example.pollypress.dashboards.ReporterDashboardActivity;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.VolleySingleton;
import com.example.pollypress.dashboards.AdminDashboardActivity;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private EditText emailEditText, passwordEditText;
    private Spinner roleSpinner;
    private MaterialButton loginButton;
    private TextView signupTextView, forgotPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        loginButton = findViewById(R.id.loginButton);
        signupTextView = findViewById(R.id.signupTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        String[] roles = {"newsuser", "admin", "reporter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedRole = (String) parent.getItemAtPosition(position);
                forgotPasswordTextView.setVisibility(selectedRole.equals("newsuser") ? android.view.View.VISIBLE : android.view.View.GONE);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                forgotPasswordTextView.setVisibility(android.view.View.GONE);
            }
        });

        loginButton.setOnClickListener(v -> performLogin());
        signupTextView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignupActivity.class)));
        forgotPasswordTextView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class)));
    }

    private void performLogin() {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String role = roleSpinner.getSelectedItem().toString();

        if (email.isEmpty() || password.isEmpty()) {
            TextView errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setText("Please enter email and password");
            return;
        }

        switch (role) {
            case "reporter":
                fetchReporterDetails(email, password);
                break;
            case "newsuser":
                fetchNewsUserDetails(email, password);
                break;
            case "admin":
                fetchAdminDetails(email, password);
                break;
        }
    }

    private void fetchReporterDetails(final String email, final String password) {
        String url = BASE_URL + "reporters";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("email").equalsIgnoreCase(email)
                                    && obj.getString("password").equals(password)) {
                                int id = obj.getInt("id");
                                String username = obj.getString("username");
                                ReporterSession session = new ReporterSession(MainActivity.this);
                                // <— right overload
                                session.saveReporterDetails(id, username, email, password);
                                startActivity(new Intent(MainActivity.this, ReporterDashboardActivity.class));
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(MainActivity.this, "Invalid reporter credentials", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing reporter data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Failed to fetch reporter details", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchNewsUserDetails(final String email, final String password) {
        String url = BASE_URL + "newsusers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("email").equalsIgnoreCase(email) &&
                                    obj.getString("password").equals(password)) {

                                int id = obj.getInt("id");
                                String username = obj.getString("username");
                                String fetchedEmail = obj.getString("email"); // ✅ renamed
                                String passwordFromApi = obj.getString("password");

                                NewsUserSession session = new NewsUserSession(MainActivity.this);
                                session.saveUserDetails(id, username, fetchedEmail, passwordFromApi);

                                startActivity(new Intent(MainActivity.this, NewsUserDashboardActivity.class));
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(this, "Invalid news user credentials", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing news user data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Failed to fetch news user details", Toast.LENGTH_SHORT).show());
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }




    private void fetchAdminDetails(final String email, final String password) {
        String url = VolleySingleton.BASE_URL + "admins";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String apiEmail    = obj.getString("email");
                            String apiPassword = obj.getString("password");

                            if (apiEmail.equalsIgnoreCase(email) && apiPassword.equals(password)) {
                                int id          = obj.getInt("id");
                                String username = obj.getString("username");

                                // Save id, username, and email
                                AdminSession session = new AdminSession(MainActivity.this);
                                session.saveAdmin(id, username, email);

                                // Go to Admin dashboard
                                startActivity(new Intent(MainActivity.this, AdminDashboardActivity.class));
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(MainActivity.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing admin data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Failed to fetch admin details", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

}