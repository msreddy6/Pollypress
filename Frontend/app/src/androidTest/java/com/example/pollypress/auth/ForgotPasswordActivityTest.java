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
public class ForgotPasswordActivityTest {

    @Test
    public void uiElementsAreDisplayed_andClickWithEmptyEmailDoesNotCrash() {
        try (ActivityScenario<ForgotPasswordActivity> scenario =
                     ActivityScenario.launch(ForgotPasswordActivity.class)) {

            // Verify the email input and reset button are visible
            onView(withId(R.id.emailEditText))
                    .check(matches(isDisplayed()));
            onView(withId(R.id.forgotPasswordButton))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Reset Password")));

            // Click the button with empty email – should show a Toast and not crash
            onView(withId(R.id.forgotPasswordButton))
                    .perform(click());
        }
    }
}
