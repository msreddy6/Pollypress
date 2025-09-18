package com.example.pollypress.dashboards;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class ReporterProfileActivityTest {

    @Test
    public void profileUiElements_areDisplayed() {
        // 1) Pre-load a fake ReporterSession so onCreate() shows the greeting
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences("ReporterSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("reporterId", 1)
                .putString("reporterUsername", "testUser")
                .putString("reporterEmail", "test@example.com")
                .putString("reporterPassword", "password")
                .apply();

        // 2) Launch the activity
        try (ActivityScenario<ReporterProfileActivity> scenario =
                     ActivityScenario.launch(ReporterProfileActivity.class)) {

            // 3) Verify greeting TextView shows "Hi, testUser" and email on next line
            onView(withId(R.id.tvGreeting))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Hi, testUser\ntest@example.com")));

            // 4) Verify profile image is visible
            onView(withId(R.id.profileImage))
                    .check(matches(isDisplayed()));

            // 5) Verify all buttons with correct labels
            onView(withId(R.id.btnEditProfile))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Edit Profile")));
            onView(withId(R.id.btnShareApp))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Share App")));
            onView(withId(R.id.btnLogout))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Logout")));
            onView(withId(R.id.btnDeleteAccount))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Delete Account")));
            onView(withId(R.id.btnBackHome))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("← Back to Home")));
        }
    }
}
