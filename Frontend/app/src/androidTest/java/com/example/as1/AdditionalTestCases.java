package com.example.as1;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;

import androidx.test.core.app.ActivityScenario;
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

//Written by Benjamin Towle

@RunWith(AndroidJUnit4.class)
public class AdditionalTestCases {

    @Before
    public void setUpIntents() {
        Intents.init();
    }
    @After
    public void cleanUpIntents() {
        Intents.release();
    }

    public ActivityScenarioRule<AddClassesDetailsActivity> activityRule = new ActivityScenarioRule<>(AddClassesDetailsActivity.class);

    @Test
    public void testEmptyTextInputAddClasses() {
        ActivityScenario<AddClassesDetailsActivity> scenario = ActivityScenario.launch(AddClassesDetailsActivity.class);

        //Mock scenario of user inputs
        Espresso.onView(withId(R.id.credits_input)).perform(typeText("4"));
        Espresso.onView(withId(R.id.course_location_input)).perform(typeText("testCaseCourse"));
        Espresso.onView(withId(R.id.course_name_input)).perform(typeText("testCaseLocation"));
        Espresso.onView(withId(R.id.start_time_input)).perform(typeText("1:00"));
        onView(isRoot()).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.radio_fall)).perform(click());
        Espresso.onView(withId(R.id.check_tuesday)).perform(click());
        Espresso.onView(withId(R.id.next_classes_added_btn)).perform(click());


        Intents.intended(not(IntentMatchers.hasComponent(AddClassesActivity.class.getName())));
        scenario.close();



    }

    @Test
    public void testEmptyButtonInputAddClasses() {

        ActivityScenario<AddClassesDetailsActivity> scenario = ActivityScenario.launch(AddClassesDetailsActivity.class);

        //Mock scenario of user inputs
        Espresso.onView(withId(R.id.credits_input)).perform(typeText("3"));
        Espresso.onView(withId(R.id.course_location_input)).perform(typeText("testCaseCourse1"));
        Espresso.onView(withId(R.id.course_name_input)).perform(typeText("testCaseLocation1"));
        Espresso.onView(withId(R.id.start_time_input)).perform(typeText("4:00"));
        onView(isRoot()).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.radio_fall)).perform(click());
        Espresso.onView(withId(R.id.end_time_input)).perform(typeText("5:00"));
        onView(isRoot()).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.next_classes_added_btn)).perform(click());
        Intents.intended(not(IntentMatchers.hasComponent(AddClassesActivity.class.getName())));
        scenario.close();

    }



    @Test
    public void testCompleteFieldsAddClasses() {
        ActivityScenario<AddClassesDetailsActivity> scenario = ActivityScenario.launch(AddClassesDetailsActivity.class);

        //Mock scenario of user inputs
        Espresso.onView(withId(R.id.credits_input)).perform(typeText("4"));
        Espresso.onView(withId(R.id.course_location_input)).perform(typeText("testCaseCourse"));
        Espresso.onView(withId(R.id.course_name_input)).perform(typeText("testCaseLocation"));
        Espresso.onView(withId(R.id.start_time_input)).perform(typeText("1:00"));
        onView(isRoot()).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.radio_fall)).perform(click());
        Espresso.onView(withId(R.id.end_time_input)).perform(typeText("2:00"));
        onView(isRoot()).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.check_tuesday)).perform(click());
        onView(isRoot()).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.next_classes_added_btn)).perform(click());


        Intents.intended(IntentMatchers.hasComponent(AddClassesActivity.class.getName()));
        scenario.close();

    }





}
