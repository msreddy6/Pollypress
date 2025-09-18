package com.example.pollypress.dashboards;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class NewTicketActivityTest {

    @Before
    public void setUpReporterSession() {
        // Ensure ReporterSession has a valid reporterId so createTicket() won't finish()
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences("ReporterSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("reporterId", 99)
                .putString("reporterUsername", "testUser")
                .apply();
    }

    @Test
    public void spinnerAndButton_areDisplayed() {
        try (ActivityScenario<NewTicketActivity> scenario =
                     ActivityScenario.launch(NewTicketActivity.class)) {

            // Verify the admin-selection spinner is visible
            onView(withId(R.id.spinnerAdmins))
                    .check(matches(isDisplayed()));

            // Verify the Submit button is visible with correct label
            onView(withId(R.id.btnSubmitTicket))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Submit")));
        }
    }
}
