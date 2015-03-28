package com.logggly.utilities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Hafiz Waleed Hussain on 3/28/2015.
 */
public class SpeechUtility {

    public static void speechRequest(Fragment fragment, int ON_ACTIVITY_RESULT_CODE){

        Intent listenIntent = getIntent(fragment.getClass());
            try {
                fragment.startActivityForResult(listenIntent, ON_ACTIVITY_RESULT_CODE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(fragment.getActivity(),
                        "Speech not supported",
                        Toast.LENGTH_SHORT).show();
            }
    }


    public static void speechRequest(Activity activity, int ON_ACTIVITY_RESULT_CODE) {
        Intent listenIntent = getIntent(activity.getClass());
        try {
            activity.startActivityForResult(listenIntent, ON_ACTIVITY_RESULT_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(activity,
                    "Speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static Intent getIntent(Class clazz) {
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, clazz.getPackage().getName());
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        return listenIntent;
    }

        public static String parseSpeechAsString(Intent data){
        ArrayList<String> result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        return result.get(0);
    }

}
