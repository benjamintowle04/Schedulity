package com.example.as1;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FriendsTest {

    @Before
    public void setUpIntents() {
        Intents.init();
    }
    @After
    public void cleanUpIntents() {
        Intents.release();
    }


    @Rule
    public ActivityScenarioRule<FriendsActivity> activityRule = new ActivityScenarioRule<>(FriendsActivity.class);

    @Test
    public void testOpenAddFriendsActivity() {
        // Initialize Intents for testing
        //Intents.init();

        // Perform a click action on the button
        Espresso.onView(withId(R.id.AddFriendsImageBtn)).perform(click());

        // Verify if the SecondActivity was opened
        Intents.intended(IntentMatchers.hasComponent(AddFriendsActivity.class.getName()));

        // Release Intents after testing
       // Intents.release();
    }

    @Test
    public void testOpenDeleteFriendsActivity() {
        // Initialize Intents for testing
        //Intents.init();

        // Perform a click action on the button
        Espresso.onView(withId(R.id.DeleteFriendsImageBtn)).perform(click());

        // Verify if the SecondActivity was opened
        Intents.intended(IntentMatchers.hasComponent(RemoveFriendsActivity.class.getName()));

        // Release Intents after testing
        //Intents.release();
    }
}
