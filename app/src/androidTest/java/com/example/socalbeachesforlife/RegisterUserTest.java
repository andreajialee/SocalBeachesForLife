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
import com.example.socalbeachesforlife.activities.MapsActivity;
import com.example.socalbeachesforlife.activities.RegisterUser;

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
public class RegisterUserTest {
    @Rule
    public ActivityScenarioRule<RegisterUser> mActivityRule
            = new ActivityScenarioRule<>(RegisterUser.class);

    private View decorView;

    @Before
    public void setUp() throws Exception {
        mActivityRule.getScenario().onActivity(new ActivityScenario.ActivityAction<RegisterUser>() {
            @Override
            public void perform(RegisterUser activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });

        // Clear all tests
        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.verify_password)).perform(ViewActions.clearText());
    }

    @After
    public void tearDown() {
        mActivityRule.getScenario().close();
    }

    @Test
    public void testEmptyNameField() {
        String warning = "Full Name is Required!";
        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testEmptyEmailField() {
        String warning = "Email is Required!";

        String name = "name";
        String password = "password";
        String verify = "p@ssword";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.verify_password)).perform(ViewActions.typeText(verify));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testInvalidEmailField() {
        String warning = "Please provide valid email.";
        String name = "name";
        String email = "email";
        String password = "password";
        String verify = "password";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.verify_password)).perform(ViewActions.typeText(verify));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testEmptyPasswordField() {
        String warning = "Password is Required!";
        String name = "name";
        String email = "user@gmail.com";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testShortPasswordField() {
        String warning = "Please set password longer than 6 characters.";
        String name = "name";
        String email = "user@gmail.com";
        String password = "pass";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testEmptyVerifyPasswordField() {
        String warning = "Verify Password is Required!";

        String name = "name";
        String email = "user@gmail.com";
        String password = "password";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testUnmatchedPasswords() {
        String warning = "Passwords do not match!";
        String name = "name";
        String email = "user@gmail.com";
        String password = "password";
        String verify = "p@ssword";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.verify_password)).perform(ViewActions.typeText(verify));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testExistingUser() {
        String warning = "Failed to register. Try Again.";
        String name = "Andrea Lee";
        String email = "endrealee@gmail.com";
        String password = "password";
        String verify = "password";

        Espresso.onView(ViewMatchers.withId(R.id.full_name)).perform(ViewActions.typeText(name));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText(email));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText(password));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.verify_password)).perform(ViewActions.typeText(verify));
        Espresso.closeSoftKeyboard();

        Espresso.onView(ViewMatchers.withId(R.id.register_button)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText(warning))
                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}