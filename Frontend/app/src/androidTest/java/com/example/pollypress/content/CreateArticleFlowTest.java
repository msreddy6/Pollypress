package com.example.pollypress.content;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.pollypress.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class CreateArticleFlowTest {

    @Before
    public void setUp() {
        // Prevent CreateArticleActivity.finish() during tests
        CreateArticleActivity.isInTestMode = true;
    }

    @After
    public void tearDown() {
        CreateArticleActivity.isInTestMode = false;
    }

    @Test
    public void testSubmitDisplaysSuccessMessage() throws InterruptedException {
        ActivityScenario.launch(CreateArticleActivity.class);

        onView(withId(R.id.etTitle))
                .perform(typeText("Test Title"), closeSoftKeyboard());
        onView(withId(R.id.etContent))
                .perform(typeText("Test Content"), closeSoftKeyboard());
        onView(withId(R.id.etPublication))
                .perform(typeText("Test Pub"), closeSoftKeyboard());
        onView(withId(R.id.etDate))
                .perform(typeText("2025-05-01"), closeSoftKeyboard());

        onView(withId(R.id.btnSubmit)).perform(click());

        // Allow the UI to update
        Thread.sleep(2000);

        onView(withId(R.id.tvSuccess))
                .check(matches(withText("Article created successfully")))
                .check(matches(isDisplayed()));
    }
}
