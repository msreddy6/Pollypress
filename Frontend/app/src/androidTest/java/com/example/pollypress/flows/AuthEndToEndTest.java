package com.example.pollypress.flows;

import com.example.pollypress.R;
import com.example.pollypress.auth.MainActivity;
import com.example.pollypress.auth.SignupActivity;
import com.example.pollypress.auth.ForgotPasswordActivity;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AuthEndToEndTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void initialUIComponents_areDisplayed() {
        // Email & Password fields, spinner, buttons & links :contentReference[oaicite:6]{index=6}:contentReference[oaicite:7]{index=7}
        onView(withId(R.id.emailEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.roleSpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.signupTextView)).check(matches(isDisplayed()));
        // Default role is "newsuser", so forgot-password link should be visible :contentReference[oaicite:8]{index=8}:contentReference[oaicite:9]{index=9}
        onView(withId(R.id.forgotPasswordTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void emptyFields_showsError() {
        // Tap login with empty email/password :contentReference[oaicite:10]{index=10}:contentReference[oaicite:11]{index=11}
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.errorTextView))
                .check(matches(withText("Please enter email and password")));     // :contentReference[oaicite:12]{index=12}:contentReference[oaicite:13]{index=13}
    }

    @Test
    public void openSignup_opensSignupActivity() {
        // Tap "Don't have an account? Signup" :contentReference[oaicite:14]{index=14}:contentReference[oaicite:15]{index=15}
        onView(withId(R.id.signupTextView)).perform(click());
        intended(hasComponent(SignupActivity.class.getName()));
    }

    @Test
    public void spinnerSelection_hidesOrShowsForgotPassword() {
        // Select "admin" role → forgot-password hidden :contentReference[oaicite:16]{index=16}:contentReference[oaicite:17]{index=17}
        onView(withId(R.id.roleSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("admin"))).perform(click());
        onView(withId(R.id.forgotPasswordTextView)).check(matches(not(isDisplayed())));

        // Select back "newsuser" → forgot-password re-shown :contentReference[oaicite:18]{index=18}:contentReference[oaicite:19]{index=19}
        onView(withId(R.id.roleSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("newsuser"))).perform(click());
        onView(withId(R.id.forgotPasswordTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void openForgotPassword_opensForgotPasswordActivity() {
        // Ensure "newsuser" selected then tap "I forgot my password" :contentReference[oaicite:20]{index=20}:contentReference[oaicite:21]{index=21}
        onView(withId(R.id.roleSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("newsuser"))).perform(click());
        onView(withId(R.id.forgotPasswordTextView)).perform(click());
        intended(hasComponent(ForgotPasswordActivity.class.getName()));
    }
}
