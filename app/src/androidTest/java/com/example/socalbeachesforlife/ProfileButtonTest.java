package com.example.socalbeachesforlife;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.socalbeachesforlife.activities.MapsActivity;
import com.example.socalbeachesforlife.activities.Profile;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class ProfileButtonTest {

    @Rule
    public ActivityTestRule<Profile> mActivityRule = new ActivityTestRule(Profile.class);
    //public IntentsTestRule<Profile> mActivityRule = new IntentsTestRule(Profile.class);

    public ProfileButtonTest() {
        IdlingPolicies.setMasterPolicyTimeout(5000, TimeUnit.SECONDS);
    }

    @Test
    public void ManageReviewButton() {
        onView(withId(R.id.manage_review)).check(matches(isClickable()));
    }

    @Test
    public void AddReviewButton() {
        onView(withId(R.id.add_review)).check(matches(isClickable()));
    }

    /* this is a unit †est
    @Test
    public void ProfileActivity(){
        onView(withId(R.id.profile_button)).perform(click());
        intended(hasComponent(Profile.class.getName()));
    }

     */
}
