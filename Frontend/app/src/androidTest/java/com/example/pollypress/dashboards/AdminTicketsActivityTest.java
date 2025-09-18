package com.example.pollypress.dashboards;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class AdminTicketsActivityTest {

    @Before
    public void setUpSession() {
        // Pre-load a fake AdminSession so fetchTickets builds a valid URL
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences(
                "AdminSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("adminId", 1)
                .putString("adminUsername", "adminUser")
                .apply();
    }

    @Test
    public void recyclerViewIsDisplayed_andActivityDoesNotCrash() {
        // Launch the AdminTicketsActivity
        try (ActivityScenario<AdminTicketsActivity> scenario =
                     ActivityScenario.launch(AdminTicketsActivity.class)) {

            // Verify that the RecyclerView is present
            onView(withId(R.id.rvAdminTickets))
                    .check(matches(isDisplayed()));
        }
    }
}
