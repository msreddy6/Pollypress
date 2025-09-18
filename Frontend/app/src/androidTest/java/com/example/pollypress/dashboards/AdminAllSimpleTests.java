package com.example.pollypress.dashboards;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.AdminSession;
import com.example.pollypress.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AdminAllSimpleTests {

    @Before
    public void setUpSession() {
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences("AdminSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("adminUsername", "testAdmin")
                .putInt("adminId", 1)
                .apply();
    }

    @Test
    public void testSessionPersistence() {
        Context ctx = ApplicationProvider.getApplicationContext();
        AdminSession session = new AdminSession(ctx);
        assertEquals(1, session.getAdminId());
        assertEquals("testAdmin", session.getAdminUsername());
    }

    @Test
    public void testDrawerGreeting() {
        try (ActivityScenario<AdminDashboardActivity> scenario =
                     ActivityScenario.launch(AdminDashboardActivity.class)) {
            onView(withId(R.id.drawerLayout))
                    .perform(DrawerActions.open());
            onView(withId(R.id.tvUsername))
                    .check(matches(withText("Hi, testAdmin")));
        }
    }

    @Test
    public void testTicketsButtonVisible() {
        try (ActivityScenario<AdminDashboardActivity> scenario =
                     ActivityScenario.launch(AdminDashboardActivity.class)) {
            onView(withId(R.id.btnAdminTickets))
                    .perform(scrollTo())
                    .check(matches(withText("Tickets")));
        }
    }

    @Test
    public void testAdminTicketsRecyclerView() {
        try (ActivityScenario<AdminTicketsActivity> scenario =
                     ActivityScenario.launch(AdminTicketsActivity.class)) {
            onView(withId(R.id.rvAdminTickets))
                    .check(matches(isDisplayed()));
            // above asserts view exists; you can add isDisplayed() if desired
        }
    }
}
