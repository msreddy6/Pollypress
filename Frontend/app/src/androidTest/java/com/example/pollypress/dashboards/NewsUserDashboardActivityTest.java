package com.example.pollypress.dashboards;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class NewsUserDashboardActivityTest {

    @Test
    public void dashboard_uiElementsAreDisplayed() {
        // 1) Pre-load a fake NewsUserSession so onCreate() shows "Hi, testUser"
        Context ctx = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = ctx
                .getSharedPreferences("NewsUserSession", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("username", "testUser")
                .apply();

        // 2) Launch the activity
        try (ActivityScenario<NewsUserDashboardActivity> scenario =
                     ActivityScenario.launch(NewsUserDashboardActivity.class)) {

            // 3) Open the navigation drawer to reveal the username
            onView(withId(R.id.drawerLayout))
                    .perform(DrawerActions.open());

            // 4) Verify greeting text in drawer
            onView(withId(R.id.tvUsername))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Hi, testUser")));

            // 5) Verify bottom-nav buttons are visible
            onView(withId(R.id.btnNewsArticles))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("News Articles")));
            onView(withId(R.id.btnPolls))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Polls")));

            // 6) Verify ViewPager2 and SwipeRefreshLayout are present
            onView(withId(R.id.viewPager))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.swipeRefreshPolls))
                    .check(matches(isDisplayed()));
        }
    }
}
