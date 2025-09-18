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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class EditPollActivityTest {

    @Test
    public void fieldsArePrePopulated_andButtonsAreVisible() {
        // Prepare an Intent with dummy poll data
        Context appCtx = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(appCtx, EditPollActivity.class)
                .putExtra("pollId", 555L)
                .putExtra("title", "Favorite Color?")
                .putExtra("description", "Choose one:")
                .putExtra("options", "[Red, Green, Blue]");

        // Launch the activity with that Intent
        try (ActivityScenario<EditPollActivity> scenario =
                     ActivityScenario.launch(intent)) {

            // Verify each EditText shows the passed-in value (options string should be cleaned)
            onView(withId(R.id.etEditPollTitle))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Favorite Color?")));

            onView(withId(R.id.etEditPollDescription))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Choose one:")));

            onView(withId(R.id.etEditPollOptions))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Red, Green, Blue")));

            // Verify Update and Delete buttons are present
            onView(withId(R.id.btnUpdatePoll))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Update")));

            onView(withId(R.id.btnDeletePoll))
                    .check(matches(isDisplayed()))
                    .check(matches(withText("Delete")));
        }
    }
}
