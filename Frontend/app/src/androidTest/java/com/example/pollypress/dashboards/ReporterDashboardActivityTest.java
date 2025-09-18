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
public class ReporterDashboardActivityTest {

    @Test
    public void createButtons_areDisplayed() {
        // Pre-load a fake ReporterSession so onCreate() doesn't finish()
        Context appCtx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = appCtx
                .getSharedPreferences("ReporterSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("reporterUsername", "testUser")
                .putInt("reporterId", 42)
                .apply();

        // Launch the activity
        try (ActivityScenario<ReporterDashboardActivity> scenario =
                     ActivityScenario.launch(ReporterDashboardActivity.class)) {

            // Verify "+ Article" button is visible
            onView(withId(R.id.btnCreateArticle))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("+ Article")));

            // Verify "+ Poll" button is visible
            onView(withId(R.id.btnCreatePoll))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("+ Poll")));
        }
    }
}
