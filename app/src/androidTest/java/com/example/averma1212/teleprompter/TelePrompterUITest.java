package com.example.averma1212.teleprompter;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by HP on 10-01-2018.
 */

public class TelePrompterUITest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource(){
        idlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void main_to_detail_testing() {
        onView(ViewMatchers.withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

        onView((withId(R.id.playButton))).check(matches(withText("Play")));
        onView((withId(R.id.removeButton))).check(matches(withText("Delete")));

    }

    @Test
    public void main_to_add_testing() {
        onView(ViewMatchers.withId(R.id.add_fab))
                .perform(click());

        onView((withId(R.id.addButton))).check(matches(withText("Add")));
        onView((withId(R.id.discardButton))).check(matches(withText("Discard")));

        // From detail to main then again to detail
        onView(ViewMatchers.withId(R.id.discardButton))
                .perform(click());
        onView(ViewMatchers.withId(R.id.add_fab))
                .perform(click());
        onView((withId(R.id.addButton))).check(matches(withText("Add")));
    }




        @After
    public void unregisterIdlingResource() {
        if (idlingResource != null)
            Espresso.unregisterIdlingResources(idlingResource);
    }
}
