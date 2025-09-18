package com.example.pollypress.content;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pollypress.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class EditArticleActivityTest {

    @Test
    public void fieldsArePrePopulated_andButtonsAreVisible() {
        // Prepare an Intent with dummy article data
        Context appCtx = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(appCtx, EditArticleActivity.class)
                .putExtra("articleId", 123)
                .putExtra("title", "Test Title")
                .putExtra("content", "Some test content")
                .putExtra("author", "Alice")
                .putExtra("publication", "UnitTest Journal")
                .putExtra("publicationDate", "2025-05-06");

        // Launch the activity with that Intent
        try (ActivityScenario<EditArticleActivity> scenario =
                     ActivityScenario.launch(intent)) {

            // Verify each EditText shows the passed-in value
            onView(withId(R.id.etEditTitle))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Test Title")));

            onView(withId(R.id.etEditContent))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Some test content")));

            onView(withId(R.id.etEditAuthor))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Alice")));

            onView(withId(R.id.etEditPublication))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("UnitTest Journal")));

            onView(withId(R.id.etEditDate))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("2025-05-06")));

            // Verify Update and Delete buttons are present
            onView(withId(R.id.btnUpdate))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Update")));

            onView(withId(R.id.btnDelete))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Delete")));
        }
    }
}
