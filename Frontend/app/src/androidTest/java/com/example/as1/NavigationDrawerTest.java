package com.example.as1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.Gravity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavigationDrawerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testNavigationTOpen() {
        // Check drawer is closed
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Check if drawer is open
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)));
    }

    @Test
    public void testNavigationToUserScreen() {
        // Initialize Intents for testing
        Intents.init();

        // Check drawer is closed
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Check if drawer is open
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)));

        // Perform a click action on the button
        Espresso.onView(withId(R.id.nav_user)).perform(click());



        // Verify if the SecondActivity was opened
        Intents.intended(IntentMatchers.hasComponent(UserActivity.class.getName()));

        // Release Intents after testing
        Intents.release();
    }

    @Test
    public void testNavigationToJourneyScreen() {
        // Initialize Intents for testing
        Intents.init();

        // Check drawer is closed
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)));

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Check if drawer is open
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)));

        // Perform a click action on the button
        Espresso.onView(withId(R.id.journey)).perform(click());

        // Verify if the SecondActivity was opened
        Intents.intended(IntentMatchers.hasComponent(GymHoursActivity.class.getName()));

        // Release Intents after testing
        Intents.release();
    }

}
