package com.example.socalbeachesforlife;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.socalbeachesforlife.activities.MainActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

@RunWith(JUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    private View decorView;

    @Before
    public void setUp() throws Exception {
        mActivityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });

        // Clear all tests
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.clearText());


    }

    @After
    public void tearDown() {
        mActivityRule.getScenario().close();
    }

    @Test
    public void testEmptyEmailField() {
        String warning = "Email is Required!";
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testInvalidEmailField() {
        String warning = "Please provide valid email.";
        String invalidEmail = "email";

        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(invalidEmail));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testEmptyPasswordField() {
        String warning = "Password is Required!";
        String email = "user@gmail.com";

        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testShortPasswordField() {
        String warning = "Please enter a password longer than 6 characters.";
        String email = "user@gmail.com";
        String password = "pass";

        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testLoginFail() {
        String warning = "Failed to login. Please check your credentials";
        String email = "invaliduser@gmail.com";
        String password = "invalidpassword";

        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testLoginSuccessful() {
        String warning = "Login Successful!";
        String email = "endrealee@gmail.com";
        String password = "andrealee";

        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}