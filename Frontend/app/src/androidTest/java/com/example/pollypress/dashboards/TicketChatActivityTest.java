package com.example.pollypress.dashboards;

import android.content.Context;
import android.content.Intent;
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
public class TicketChatActivityTest {

    @Before
    public void setUpSession() {
        // Pre-load a fake ReporterSession so onCreate() picks up a username
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences(
                "ReporterSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("reporterUsername", "testUser")
                .putInt("reporterId", 42)
                .apply();
    }

    @Test
    public void uiElementsAreDisplayed_andSendEmptyMessageDoesNotCrash() {
        // Prepare Intent with a valid ticketId
        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                TicketChatActivity.class)
                .putExtra("ticketId", 99L);

        // Launch the activity
        try (ActivityScenario<TicketChatActivity> scenario =
                     ActivityScenario.launch(intent)) {

            // Check that RecyclerView for messages is visible
            onView(withId(R.id.rvMessages))
                    .check(matches(isDisplayed()));

            // Check that message input field is visible
            onView(withId(R.id.etMessage))
                    .check(matches(isDisplayed()));

            // Check that Send button is visible and has correct text
            onView(withId(R.id.btnSend))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Send")));

            // Click Send with empty input—should not crash
            onView(withId(R.id.btnSend))
                    .perform(click());
        }
    }
}
