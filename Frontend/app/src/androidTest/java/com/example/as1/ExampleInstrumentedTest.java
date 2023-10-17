package com.example.as1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

import com.example.as1.Controller.ActivityController;
import com.example.as1.Enum.LoggedInStates;
import com.example.as1.Models.User;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Before
    public void setUpIntents() {
        Intents.init();
    }
    @After
    public void cleanUpIntents() {
        Intents.release();
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.as1", appContext.getPackageName());
    }


    @Rule
    public ActivityScenarioRule<RegistrationActivity> rule = new ActivityScenarioRule<RegistrationActivity>(RegistrationActivity.class);

    @Test
    public void testRegister() {
        //Intents.init();
        ActivityScenario<RegistrationActivity> scenario = ActivityScenario.launch(RegistrationActivity.class);
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.advisorTextViewRegister)).perform(click());
        onView(withId(R.id.userNameEdittext)).perform(typeText("bobby"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.emailEdittext)).perform(typeText("bobby"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordTextEdit)).perform(typeText("bobby"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.repeatepasswordTextedit)).perform(typeText("bob")); //Testing if it knows when passwords don't match
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());


        // To verify that an activity is not launched
        Intents.intended(not(hasComponent(MainActivity.class.getName())));
        scenario.close();
        //Intents.release();


    }

    ActivityScenarioRule<GymHoursActivity> gymRule = new ActivityScenarioRule<GymHoursActivity>(GymHoursActivity.class);

    @Test
    public void testGymHoursWithNoInputText() {

        //Intents.init();
        ActivityScenario<GymHoursActivity> scenario = ActivityScenario.launch(GymHoursActivity.class);
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.next_btn_gym)).perform(click());

        Intents.intended(not(hasComponent(AddClassesActivity.class.getName())));
        scenario.close();
        //Intents.release();

    }

    ActivityScenarioRule<LoginActivity> loginRule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);
    @Test
    public void testParentLoginChangeText() {
        //Intents.init();
        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.parentTextView)).perform(click());

        String expectedText = "Parent Login";
        onView(withId(R.id.login_text_view))
                .check(matches(withText(expectedText)));

        //Intents.release();
    }



}