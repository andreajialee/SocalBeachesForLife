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

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.socalbeachesforlife.activities.MainActivity;
import com.example.socalbeachesforlife.activities.MapsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

public final class NavigationTests {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() {
        Intents.init();
    }

    @After
    public void after() {
        Intents.release();
    }
    @Test
    public void ManageLoginButton() {
        onView(withId(R.id.loginButton)).check(matches(isClickable()));
    }


}