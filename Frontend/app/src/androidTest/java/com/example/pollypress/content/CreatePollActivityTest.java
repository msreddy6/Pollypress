package com.example.pollypress.content;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;

import com.example.pollypress.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class CreatePollActivityTest {

    @Before
    public void enableTestMode() {
        // Prevent Activity from finishing immediately in tests
        CreatePollActivity.isInTestMode = true;
    }

    @Test
    public void allFieldsAndButtonAreVisible_andSubmitDoesNotCrash() {
        try (ActivityScenario<CreatePollActivity> scenario =
                     ActivityScenario.launch(CreatePollActivity.class)) {

            // Verify that all input fields and the submit button are shown
            onView(withId(R.id.etPollTitle))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.etPollDescription))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.etPollOptions))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.btnSubmitPoll))
                    .check(matches(isDisplayed()));

            // Click the Submit button with empty fields—there should be no crash
            onView(withId(R.id.btnSubmitPoll))
                    .perform(click());
        }
    }
}
