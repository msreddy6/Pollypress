package com.example.pollypress.dashboards;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.pollypress.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class AdminDashboardActivityTest {

    @Rule
    public ActivityScenarioRule<AdminDashboardActivity> activityRule =
            new ActivityScenarioRule<>(AdminDashboardActivity.class);

    @Test
    public void ticketsButton_isDisplayed() {
        onView(withId(R.id.btnAdminTickets))
                .perform(scrollTo())                   // scroll down to it
                .check(matches(isDisplayed()))
                .check(matches(withText("Tickets")));
    }
}
