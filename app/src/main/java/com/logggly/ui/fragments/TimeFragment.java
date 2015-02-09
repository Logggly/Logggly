package com.logggly.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Hafiz Waleed Hussain on 1/29/2015.
 */
public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public static final String TIME= "time";

    public static interface Callback{
        void updateTimeInstance(Calendar mCalendar);
    }

    public static TimeFragment newInstance(Calendar calendar){
        TimeFragment dateFragment = new TimeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TIME, calendar);
        dateFragment.setArguments(bundle);
        return dateFragment;
    }

    private Calendar mCalendar;
    private Callback mCallback;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mCalendar = (Calendar) bundle.getSerializable(TIME);
        if( mCalendar == null){
            throw new NullPointerException("Require a Calendar instance");
        }

        int hours = mCalendar.get(Calendar.HOUR);
        int minutes = mCalendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hours,minutes,false);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
           int year = mCalendar.get(Calendar.YEAR);
           int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.set(year,month,day,hourOfDay,minute);
        if(mCallback == null){
            throw new RuntimeException("Set TimeFragment Callback");
        }
        mCallback.updateTimeInstance(mCalendar);
    }
}
