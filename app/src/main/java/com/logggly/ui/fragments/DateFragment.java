package com.logggly.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Hafiz Waleed Hussain on 1/29/2015.
 */
public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static final String DATE= "date";

    public static interface Callback{
        void updateDateInstance(Calendar mCalendar);
    }


    public static DateFragment newInstance(Calendar calendar){
        DateFragment dateFragment = new DateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE, calendar);
        dateFragment.setArguments(bundle);
        return dateFragment;
    }

    private Calendar mCalendar;
    private Callback mCallback;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mCalendar = (Calendar) bundle.getSerializable(DATE);
        if( mCalendar == null){
            throw new NullPointerException("Require a Calendar instance");
        }

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
        if(mCallback == null){
            throw new RuntimeException("Set DateFragment Callback");
        }
        mCallback.updateDateInstance(mCalendar);

    }
}
