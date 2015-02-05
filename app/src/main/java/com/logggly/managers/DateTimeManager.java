package com.logggly.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.logggly.ui.fragments.DateFragment;
import com.logggly.ui.fragments.TimeFragment;

import java.util.Calendar;

/**
 * Created by Hafiz Waleed Hussain on 2/4/2015.
 */
public class DateTimeManager {

    private Calendar mCalendarReference;
    private FragmentActivity mActivityReference;
    private Fragment mFragmentReference;

    public DateTimeManager(Calendar calendarReference, FragmentActivity activityReference, Fragment fragmentReference) {
        mCalendarReference = calendarReference;
        mActivityReference = activityReference;
        mFragmentReference = fragmentReference;
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
            dateFragment.setTargetFragment(mFragmentReference,0);
            dateFragment.show(mActivityReference.getSupportFragmentManager(), null);
        }

    };

    private View.OnClickListener mSetTimeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TimeFragment timeFragment = TimeFragment.newInstance(mCalendarReference);
            timeFragment.setTargetFragment(mFragmentReference,0);
            timeFragment.show(mActivityReference.getSupportFragmentManager(),null);
        }
    };

}
