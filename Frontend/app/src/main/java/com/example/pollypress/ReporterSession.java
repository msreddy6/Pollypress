package com.example.pollypress;

import android.content.Context;
import android.content.SharedPreferences;

public class ReporterSession {

    private static final String PREF_NAME = "ReporterSession";
    private static final String KEY_REPORTER_ID = "reporterId";
    private static final String KEY_REPORTER_USERNAME = "reporterUsername";
    private static final String KEY_REPORTER_EMAIL = "reporterEmail";
    private static final String KEY_REPORTER_PASSWORD = "reporterPassword";

    private final Context context;

    public ReporterSession(Context context) {
        this.context = context;
    }

    // Save Reporter Details
    public void saveReporterDetails(int id, String username, String email, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_REPORTER_ID, id);
        editor.putString(KEY_REPORTER_USERNAME, username.toLowerCase());
        editor.putString(KEY_REPORTER_EMAIL, email);
        editor.putString(KEY_REPORTER_PASSWORD, password);
        editor.apply();
    }

    public int getReporterId() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_REPORTER_ID, -1);
    }

    public String getReporterUsername() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_REPORTER_USERNAME, null);
    }

    public String getReporterEmail() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_REPORTER_EMAIL, null);
    }

    public String getReporterPassword() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_REPORTER_PASSWORD, null);
    }

    public void clearSession() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return getReporterUsername() != null && getReporterId() != -1;
    }

    public void saveReporterDetails(int id, String username) {
    }
}
