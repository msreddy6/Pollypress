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
public class NewsUserProfileActivityTest {

    @Test
    public void profileUiElements_areDisplayed() {
        // 1) Pre-load a fake NewsUserSession so onCreate() shows the greeting
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx
                .getSharedPreferences("NewsUserSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("userId", 123)
                .putString("username", "testUser")
                .putString("email", "test@example.com")
                .apply();

        // 2) Launch the activity
        try (ActivityScenario<NewsUserProfileActivity> scenario =
                     ActivityScenario.launch(NewsUserProfileActivity.class)) {

            // 3) Verify greeting TextView shows "Hi, testUser" and email on next line
            onView(withId(R.id.tvGreeting))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Hi, testUser\ntest@example.com")));

            // 4) Verify all buttons are present with correct labels
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

            // 5) Verify profile image is visible
            onView(withId(R.id.profileImage))
                    .check(matches(isDisplayed()));
        }
    }
}
