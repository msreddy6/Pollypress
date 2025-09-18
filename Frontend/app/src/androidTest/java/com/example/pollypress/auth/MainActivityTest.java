package com.example.pollypress.auth;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void launch() {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void uiElementsAreDisplayed_andForgotPasswordToggle() {
        // Basic views present
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.roleSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.signupTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.forgotPasswordTextView)).check(matches(isDisplayed()));

        // Default ("newsuser") → forgot visible
        onView(withId(R.id.forgotPasswordTextView)).check(matches(isDisplayed()));

        // Select "reporter" from spinner
        onView(withId(R.id.roleSpinner)).perform(click());
        onData(CoreMatchers.is("reporter"))
                .inRoot(isPlatformPopup())
                .perform(click());
        // Now forgot should be gone
        onView(withId(R.id.forgotPasswordTextView)).check(matches(CoreMatchers.not(isDisplayed())));

        // Select "newsuser" again
        onView(withId(R.id.roleSpinner)).perform(click());
        onData(CoreMatchers.is("newsuser"))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.forgotPasswordTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void emptyLogin_showsErrorMessage() {
        // ensure fields empty
        onView(withId(R.id.emailEditText)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(click(), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.errorTextView))
                .check(matches(withText("Please enter email and password")))
                .check(matches(isDisplayed()));
    }
}
