package com.example.socalbeachesforlife;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.socalbeachesforlife.activities.AddReview;
import com.example.socalbeachesforlife.activities.MapsActivity;
import com.example.socalbeachesforlife.activities.Profile;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class RadiusChangeTest {
    @Rule
    public ActivityScenarioRule<MapsActivity> activityRule =
            new ActivityScenarioRule<MapsActivity>(MapsActivity.class);

    @Test
    public void radiusIsChanged()
    {
        onView(withId(R.id.radius_button)).perform(ViewActions.click());
        onView(withText("3000")).perform(ViewActions.click());
        onView(withId(R.id.radius_button)).perform(ViewActions.click());
        onView(withText("2000")).perform(ViewActions.click());
        onView(withId(R.id.radius_button)).perform(ViewActions.click());
        onView(withText("1000")).perform(ViewActions.click());
    }
}
