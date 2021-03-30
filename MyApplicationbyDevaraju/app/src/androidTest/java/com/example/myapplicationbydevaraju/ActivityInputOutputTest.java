package com.example.myapplicationbydevaraju;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivityInputOutputTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> mIntentRule = new IntentsTestRule<>(MainActivity.class);

    String errMsg = "Enter a valid phone number or email address";
    String textMsg = "keyboard input text";

    @Test
    public void textInputBackPress(){
        onView(withId(R.id.edit_message)).perform(clearText(), typeText(textMsg));
        onView(withId(R.id.button)).perform(click());
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withId(R.id.edit_message)).check(matches(withText(textMsg)));
    }

    @Test
    public void sendTextInput(){
        onView(withId(R.id.edit_message)).perform(clearText(), typeText(textMsg));
        onView(withId(R.id.button)).perform(click());

        // validate whether second activity receives an intent with user entered text
        intended(allOf(
                toPackage("com.example.myapplicationbydevaraju"),
                hasExtra("com.example.myapplicationbydevaraju.MESSAGE", textMsg)
        ));
    }

    @Test
    public void voiceInput() {
        String voiceMsg = "voice input text";

        // build the result to return when speech recognition activity is launched
        final ArrayList<String> results = new ArrayList<>();
        results.add(voiceMsg);
        Intent intent = new Intent();
        intent.putStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS, results);
        final Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        // provide response for speech recognition activity
        intending(hasAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)).respondWith(result);

        onView(withId(R.id.button_voice)).perform(click());

        onView(withId(R.id.edit_message)).check(matches(withText(voiceMsg)));
    }

    @Test
    public void phoneInput(){
        String phone = "sdfa234";
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.text_phone_email)).perform(clearText(), typeText(phone));
        onView(withId(R.id.button_confirm)).perform(click());
        onView(withId(R.id.text_phone_email)).check(matches(withHint(errMsg)));
    }

    @Test
    public void emailInput(){
        String email = "abcd";
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.text_phone_email)).perform(clearText(), typeText(email));
        onView(withId(R.id.button_confirm)).perform(click());
        onView(withId(R.id.text_phone_email)).check(matches(withHint(errMsg)));
    }
}