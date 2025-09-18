package com.example.pollypress.flows;

import com.example.pollypress.R;                                            // your app’s R
import com.example.pollypress.dashboards.NewsUserDashboardActivity;
import com.example.pollypress.dashboards.NewsUserProfileActivity;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.assertion.ViewAssertions.matches;         // ← import matches()
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NewsUserEndToEndTest {

    @Rule
    public ActivityScenarioRule<NewsUserDashboardActivity> dashboardRule =
            new ActivityScenarioRule<>(NewsUserDashboardActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void dashboardUIComponents_areDisplayed() {
        // Hamburger menu button :contentReference[oaicite:0]{index=0}:contentReference[oaicite:1]{index=1}
        onView(withId(R.id.btnMenu))
                .check(matches(isDisplayed()));

        // “News Articles” bottom-nav button :contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
        onView(withId(R.id.btnNewsArticles))
                .check(matches(isDisplayed()));

        // “Polls” bottom-nav button :contentReference[oaicite:4]{index=4}:contentReference[oaicite:5]{index=5}
        onView(withId(R.id.btnPolls))
                .check(matches(isDisplayed()));

        // Content ViewPager :contentReference[oaicite:6]{index=6}:contentReference[oaicite:7]{index=7}
        onView(withId(R.id.viewPager))
                .check(matches(isDisplayed()));
    }

    @Test
    public void openProfile_opensProfileActivity() {
        // Tap menu to open profile :contentReference[oaicite:8]{index=8}:contentReference[oaicite:9]{index=9}
        onView(withId(R.id.btnMenu)).perform(click());
        intended(hasComponent(NewsUserProfileActivity.class.getName()));      // :contentReference[oaicite:10]{index=10}:contentReference[oaicite:11]{index=11}
    }

    @Test
    public void profileBack_navigatesBackToDashboard() {
        // Open profile :contentReference[oaicite:12]{index=12}:contentReference[oaicite:13]{index=13}
        onView(withId(R.id.btnMenu)).perform(click());
        intended(hasComponent(NewsUserProfileActivity.class.getName()));      // :contentReference[oaicite:14]{index=14}:contentReference[oaicite:15]{index=15}

        // Tap “Back to Home” :contentReference[oaicite:16]{index=16}:contentReference[oaicite:17]{index=17}
        onView(withId(R.id.btnBackHome)).perform(click());

        // Confirm dashboard visible again :contentReference[oaicite:18]{index=18}:contentReference[oaicite:19]{index=19}
        onView(withId(R.id.btnNewsArticles))
                .check(matches(isDisplayed()));
    }
}
