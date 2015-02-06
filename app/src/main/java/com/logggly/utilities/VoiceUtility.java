package com.logggly.utilities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Hafiz Waleed Hussain on 2/6/2015.
 */
public class VoiceUtility implements RecognitionListener{


    public static interface Callback{
        void voiceResult(ArrayList<String> data);
        void error(String errorMessage);

    }

    private static final String LOG_TAG = "LoggglyVoice";

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mRecognizerIntent;
    private FragmentActivity mFragmentActivityReference;
    private Callback mCallback;

    private static VoiceUtility voiceUtility;

    public static VoiceUtility getInstance(FragmentActivity fragmentActivity){
        if(voiceUtility == null){
            voiceUtility = new VoiceUtility(fragmentActivity);
        }
        return voiceUtility;
    }

    private VoiceUtility(FragmentActivity fragmentActivityReference) {
        mFragmentActivityReference = fragmentActivityReference;
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    public void init(){
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mFragmentActivityReference);
        mSpeechRecognizer.setRecognitionListener(this);
        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                mFragmentActivityReference.getPackageName());
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        mSpeechRecognizer.startListening(mRecognizerIntent);
    }

    public void destroy(){
        mSpeechRecognizer.stopListening();
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        startSpeechRecognizeAfterMiliSeconds(1000);


    }

    private void startSpeechRecognizeAfterMiliSeconds(int miliSeconds) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpeechRecognizer.startListening(mRecognizerIntent);
            }
        },miliSeconds);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        if (mCallback != null) {
            mCallback.error(errorMessage);
        }
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
//        Log.d(LOG_TAG, text);
        if(mCallback!=null) {
            mCallback.voiceResult(matches);
        }
        mSpeechRecognizer.startListening(mRecognizerIntent);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:

                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                startSpeechRecognizeAfterMiliSeconds(2000);
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                startSpeechRecognizeAfterMiliSeconds(2000);
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
