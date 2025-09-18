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
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class ReporterTicketsActivityTest {

    @Before
    public void setUpReporterSession() {
        // Ensure ReporterSession has a valid reporterId so fetchTickets() won't finish()
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences("ReporterSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("reporterId", 99)
                .putString("reporterUsername", "testUser")
                .apply();
    }

    @Test
    public void spinnerAndButton_areDisplayed_andClickDoesNotCrash() {
        try (ActivityScenario<ReporterTicketsActivity> scenario =
                     ActivityScenario.launch(ReporterTicketsActivity.class)) {

            // Verify the Create Ticket button is visible with correct text
            onView(withId(R.id.btnNewTicket))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Create Ticket")));

            // Verify the RecyclerView is visible
            onView(withId(R.id.rvReporterTickets))
                    .check(matches(isDisplayed()));

            // Click the Create Ticket button — should launch NewTicketActivity without crash
            onView(withId(R.id.btnNewTicket))
                    .perform(click());
        }
    }
}
