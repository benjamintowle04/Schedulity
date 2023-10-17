//package com.example.as1;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.intent.matcher.IntentMatchers;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.runner.AndroidJUnit4;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class MenueTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Before
//    public void setUp() {
//        // Initialize Intents for testing
//        Intents.init();
//    }
//
//    @After
//    public void tearDown() {
//        // Release Intents after testing
//        Intents.release();
//    }
//
//    @Test
//    public void testMenuItems() {
//        // Click on the overflow menu button
//        onView(withContentDescription(R.string.menu_description))
//                .perform(click());
//
//        // Click on the "Weekly View" menu item
//        onView(withText("Weekly View")).perform(click());
//
//        // Verify that the WeeklyViewActivity component is opened
//        Intents.intended(IntentMatchers.hasComponent(WeeklyViewActivity.class.getName()));
//
//        // Click on the overflow menu button
//        onView(withContentDescription(R.string.menu_description))
//                .perform(click());
//
//        // Click on the "Monthly View" menu item
//        onView(withText("Monthly View")).perform(click());
//
//        // Verify that the MonthlyViewActivity component is opened
//        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
//
//        // Click on the overflow menu button
//        onView(withContentDescription(R.string.menu_description))
//                .perform(click());
//
//        // Click on the "Daily View" menu item
//        onView(withText("Daily View")).perform(click());
//
//        // Verify that the DailyViewActivity component is opened
//        Intents.intended(IntentMatchers.hasComponent(DailyViewActivity.class.getName()));
//    }
//}
