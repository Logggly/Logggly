package com.logggly.managers;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.logggly.ui.fragments.DateFragment;
import com.logggly.ui.fragments.TimeFragment;

import java.util.Calendar;

/**
 * Created by Hafiz Waleed Hussain on 2/4/2015.
 */
public class DateTimeManager {

    private Calendar mCalendarReference;
    private FragmentManager mFragmentManager;
    private TimeFragment.Callback mTimeFragmentCallback;
    private DateFragment.Callback mDateFragmentCallback;

    public DateTimeManager(Calendar calendarReference, FragmentManager fragmentManager,
                           TimeFragment.Callback timeFragmentCallback,
                           DateFragment.Callback dateFragmentCallback) {
        mCalendarReference = calendarReference;
        mFragmentManager = fragmentManager;
        mTimeFragmentCallback = timeFragmentCallback;
        mDateFragmentCallback = dateFragmentCallback;
    }

    public View.OnClickListener getSetDateButtonOnClickListener() {
        return mSetDateButtonOnClickListener;
    }

    public View.OnClickListener getSetTimeButtonOnClickListener() {
        return mSetTimeButtonOnClickListener;
    }

    private View.OnClickListener mSetDateButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateFragment dateFragment = DateFragment.newInstance(mCalendarReference);
            dateFragment.setCallback(mDateFragmentCallback);
            dateFragment.show(mFragmentManager, null);
        }

    };

    private View.OnClickListener mSetTimeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimeFragment timeFragment = TimeFragment.newInstance(mCalendarReference);
            timeFragment.setCallback(mTimeFragmentCallback);
            timeFragment.show(mFragmentManager,null);
        }
    };

}
