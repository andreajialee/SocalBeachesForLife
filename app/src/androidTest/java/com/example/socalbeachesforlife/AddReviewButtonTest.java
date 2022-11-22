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

import com.example.socalbeachesforlife.activities.AddReview;
import com.example.socalbeachesforlife.activities.MapsActivity;
import com.example.socalbeachesforlife.activities.Profile;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class AddReviewButtonTest {
    @Rule
    public ActivityTestRule<AddReview> mActivityRule = new ActivityTestRule(AddReview.class);

    public AddReviewButtonTest() {
        IdlingPolicies.setMasterPolicyTimeout(5000, TimeUnit.SECONDS);
    }

    @Test
    public void AddImageFromGalleryButton(){
        onView(withId(R.id.image_button)).check(matches(isClickable()));
    }

    @Test
    public void UploadReviewButton(){
        onView(withId(R.id.upload_review)).check(matches(isClickable()));
    }
}
