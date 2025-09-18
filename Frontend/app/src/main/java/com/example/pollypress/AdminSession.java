package com.example.pollypress;

import android.content.Context;
import android.content.SharedPreferences;

public class AdminSession {
    private static final String PREF_NAME          = "AdminSession";
    private static final String KEY_ADMIN_ID       = "adminId";
    private static final String KEY_ADMIN_EMAIL    = "adminEmail";
    private static final String KEY_ADMIN_USERNAME = "adminUsername";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public AdminSession(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // now also save username
    public void saveAdmin(int id, String username, String email) {
        editor.putInt(KEY_ADMIN_ID, id);
        editor.putString(KEY_ADMIN_USERNAME, username);
        editor.putString(KEY_ADMIN_EMAIL, email);
        editor.apply();
    }

    public int getAdminId() {
        return prefs.getInt(KEY_ADMIN_ID, -1);
    }

    public String getAdminUsername() {
        return prefs.getString(KEY_ADMIN_USERNAME, null);
    }

    public String getAdminEmail() {
        return prefs.getString(KEY_ADMIN_EMAIL, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
