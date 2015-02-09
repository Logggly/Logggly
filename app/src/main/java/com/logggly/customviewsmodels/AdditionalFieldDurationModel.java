package com.logggly.customviewsmodels;

import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.managers.DateTimeManager;
import com.logggly.ui.fragments.TimeFragment;
import com.logggly.utilities.DateTimeFormatter;

import java.util.Calendar;

/**
* Created by Hafiz Waleed Hussain on 2/9/2015.
*/
public class AdditionalFieldDurationModel {
    private DateTimeManager mDateTimeManager;
    private TextView mTextView;
    private Calendar mCalendar;


    public AdditionalFieldDurationModel(FragmentManager fragmentManager, TextView textView, Calendar calendar) {
        mTextView = textView;
        mCalendar = calendar;
        mDateTimeManager = new DateTimeManager(mCalendar,fragmentManager,mCallback,null);
        mTextView.setOnClickListener(mDateTimeManager.getSetTimeButtonOnClickListener());
        setTimeTextView();
    }

    public String getFieldName(){
        return mTextView.getTag().toString();
    }

    public String getFieldType(){
        return mTextView.getContext().getResources().getString(R.string.duration);
    }

    public String getFieldData(){
        return mCalendar.getTimeInMillis()+"";
    }

    private void setTimeTextView() {
        mTextView.setText(DateTimeFormatter.timeFormatter(mCalendar));
    }

    private TimeFragment.Callback mCallback = new TimeFragment.Callback() {
        @Override
        public void updateTimeInstance(Calendar calendar) {
            mCalendar = calendar;
            setTimeTextView();
        }
    };
}
