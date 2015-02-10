package com.logggly.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.logggly.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hafiz Waleed Hussain on 2/10/2015.
 */
public class DurationDialogFragment extends DialogFragment{


    public static final DurationDialogFragment newInstance(){
        DurationDialogFragment durationDialogFragment = new DurationDialogFragment();
        return durationDialogFragment;
    }

    private Calendar mStartTime;
    private Calendar mEndTime;
    private Callback mCallback;

    public static interface Callback{
        void durations(Calendar startTime, Calendar endTime);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_duration,null);
        final Chronometer chronometer = (Chronometer) view.findViewById(R.id.FragmentDurationDialog_chronometer);
        ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.FragmentDurationDialog_start_stop_toggle_button);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(mCallback == null){
                        throw new RuntimeException("Set DurationDialogFragment Callback");
                    }
                    mStartTime = Calendar.getInstance();
                    chronometer.setBase(mStartTime.getTimeInMillis());
                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
                            Calendar calendar = Calendar.getInstance();
                            long diff = calendar.getTimeInMillis() - chronometer.getBase();
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                            long hours = TimeUnit.MILLISECONDS.toHours(diff);
                            chronometer.setText(hours+" : "+minutes+" : "+seconds);

                        }
                    });
                    chronometer.start();
                }else{
                    mEndTime = Calendar.getInstance();
                    chronometer.stop();
                    if(mCallback == null){
                     throw new RuntimeException("Set DurationDialogFragment Callback");
                    }
                    mCallback.durations(mStartTime, mEndTime);
                    dismiss();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.duration);
        builder.setView(view);
        setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
