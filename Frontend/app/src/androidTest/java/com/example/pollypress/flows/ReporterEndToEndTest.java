package com.example.pollypress.flows;

import com.example.pollypress.R;
import com.example.pollypress.dashboards.ReporterDashboardActivity;
import com.example.pollypress.dashboards.ReporterTicketsActivity;
import com.example.pollypress.dashboards.NewTicketActivity;
import com.example.pollypress.content.CreateArticleActivity;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ReporterEndToEndTest {

    @Rule
    public ActivityScenarioRule<ReporterDashboardActivity> dashboardRule =
            new ActivityScenarioRule<>(ReporterDashboardActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void createArticleButton_opensCreateArticleActivity() {
        // “+ Article” button in dashboard (id in reporter_dashboard.xml) :contentReference[oaicite:0]{index=0}:contentReference[oaicite:1]{index=1}
        onView(withId(R.id.btnCreateArticle)).perform(click());
        intended(hasComponent(CreateArticleActivity.class.getName()));
    }

    @Test
    public void ticketsFlow_opensTicketsAndNewTicketScreens() {
        // FAB for tickets in dashboard (id in ReporterDashboardActivity) :contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
        onView(withId(R.id.btnMyTickets)).perform(click());
        intended(hasComponent(ReporterTicketsActivity.class.getName()));

        // “Create Ticket” button in tickets screen (activity_reporter_tickets.xml) :contentReference[oaicite:4]{index=4}:contentReference[oaicite:5]{index=5}
        onView(withId(R.id.btnNewTicket)).perform(click());
        intended(hasComponent(NewTicketActivity.class.getName()));
    }
}
