
package com.example.pollypress;

import android.content.Context;
import android.content.SharedPreferences;

public class NewsUserSession {
    private static final String PREF_NAME = "NewsUserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private SharedPreferences prefs;

    public NewsUserSession(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserDetails(int id, String username, String email, String password) {
        prefs.edit()
                .putInt(KEY_USER_ID, id)
                .putString(KEY_USERNAME, username)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD, password)
                .apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "NewsUser");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public String getPassword() {
        return prefs.getString(KEY_PASSWORD, null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
