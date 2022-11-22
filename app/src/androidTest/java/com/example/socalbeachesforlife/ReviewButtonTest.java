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

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReviewButtonTest {

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule(MapsActivity.class);

    public ReviewButtonTest() {
        IdlingPolicies.setMasterPolicyTimeout(5000, TimeUnit.SECONDS);
    }

    @Test
    public void ReviewButton() {
        onView(withId(R.id.reviews_button)).check(matches(isClickable()));
    }

}
