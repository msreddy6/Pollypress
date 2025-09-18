package com.example.pollypress.auth;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class SignupActivityTest {

    @Test
    public void uiElementsAreDisplayed_andClickSignupWithEmptyFieldsDoesNotCrash() {
        try (ActivityScenario<SignupActivity> scenario =
                     ActivityScenario.launch(SignupActivity.class)) {

            // Verify all input fields and spinner are visible
            onView(withId(R.id.usernameEditText))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.emailEditText))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.passwordEditText))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.confirmPasswordEditText))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.roleSpinner))
                    .check(matches(isDisplayed()));

            // Verify the signup button is visible and has correct text
            onView(withId(R.id.signupButton))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Signup")));

            // Click the signup button with all fields empty – should show a Toast and not crash
            onView(withId(R.id.signupButton))
                    .perform(click());
        }
    }
}
