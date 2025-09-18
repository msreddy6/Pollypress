package com.example.pollypress.dashboards;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.pollypress.NewsUserSession;
import com.example.pollypress.R;
import com.example.pollypress.VolleySingleton;
import com.example.pollypress.auth.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * NewsUserProfileActivity allows a news user to view and manage their profile.
 * Features include editing username/password, uploading a profile image,
 * sharing the app, logging out, and deleting the account.
 * @author Shivadhar Reddy Maddi
 */
public class NewsUserProfileActivity extends AppCompatActivity {

    private TextView tvGreeting;
    private ImageView profileImage;
    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    
    /**
     * Called when the activity is first created.
     * Binds views, initializes session, and sets up button listeners
     * for editing profile, logout, sharing, navigation, and account deletion.
     *
     * @param savedInstanceState the activity’s previous state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_user_profile);

        NewsUserSession session = new NewsUserSession(this);
        int userId = session.getUserId();
        String username = session.getUsername();
        String email = session.getEmail();

        tvGreeting = findViewById(R.id.tvGreeting);
        profileImage = findViewById(R.id.profileImage);
        tvGreeting.setText("Hi, " + username + "\n" + email);

        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnShareApp = findViewById(R.id.btnShareApp);
        Button btnBackHome = findViewById(R.id.btnBackHome);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        btnEditProfile.setOnClickListener(v -> showEditProfileDialog(userId));

        btnLogout.setOnClickListener(v -> {
            session.clearSession();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnShareApp.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out PollyPress News App!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        btnBackHome.setOnClickListener(v -> {
            startActivity(new Intent(this, NewsUserDashboardActivity.class));
            finish();
        });

        btnDeleteAccount.setOnClickListener(v -> confirmAndDeleteAccount(userId));

        // Image picker setup
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        profileImage.setImageURI(imageUri);
                    }
                });

        profileImage.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");
            imagePickerLauncher.launch(pickIntent);
        });
    }

    /**
     * Displays a dialog allowing the user to enter a new username and password.
     *
     * @param userId the ID of the current user
     */
    private void showEditProfileDialog(int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final android.view.View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        EditText etDialogUsername = dialogView.findViewById(R.id.etDialogUsername);
        EditText etDialogPassword = dialogView.findViewById(R.id.etDialogPassword);
        Button btnSaveChanges = dialogView.findViewById(R.id.btnSaveChanges);

        AlertDialog dialog = builder.create();
        btnSaveChanges.setOnClickListener(v -> {
            String newUsername = etDialogUsername.getText().toString().trim();
            String newPassword = etDialogPassword.getText().toString().trim();
            updateUser(userId, newUsername, newPassword);
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Sends a PUT request to update the user’s profile details on the server.
     *
     * @param userId   the ID of the user to update
     * @param username the new username (or empty to keep current)
     * @param password the new password (or empty to keep current)
     */
    private void updateUser(int userId, String username, String password) {
        String url = BASE_URL + "newsusers/" + userId;
        JSONObject body = new JSONObject();

        NewsUserSession session = new NewsUserSession(this);
        String currentUsername = session.getUsername();
        String currentEmail = session.getEmail();
        String currentPassword = session.getPassword();

        try {
            body.put("username", !username.isEmpty() ? username : currentUsername);
            body.put("email", currentEmail);
            body.put("password", !password.isEmpty() ? password : currentPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.PUT, url,
                response -> {
                    session.saveUserDetails(
                            userId,
                            !username.isEmpty() ? username : currentUsername,
                            currentEmail,
                            !password.isEmpty() ? password : currentPassword
                    );
                    tvGreeting.setText("Hi, " + session.getUsername() + "\n" + currentEmail);
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public byte[] getBody() {
                return body.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Prompts the user to confirm account deletion.
     *
     * @param userId the ID of the user to delete
     */
    private void confirmAndDeleteAccount(int userId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount(userId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Sends a DELETE request to remove the user’s account from the server.
     *
     * @param userId the ID of the user to delete
     */
    private void deleteAccount(int userId) {
        String url = BASE_URL + "newsusers/" + userId;
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
                    new NewsUserSession(this).clearSession();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
