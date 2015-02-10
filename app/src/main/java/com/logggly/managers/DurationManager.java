package com.logggly.managers;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.logggly.ui.fragments.DurationDialogFragment;

/**
 * Created by Hafiz Waleed Hussain on 2/4/2015.
 */
public class DurationManager {

//    private Calendar mStartCalendarReference;
//    private Calendar mEndCalendarReference;
    private FragmentManager mFragmentManager;
    private DurationDialogFragment.Callback mCallback;

    public DurationManager(FragmentManager fragmentManager,
                           DurationDialogFragment.Callback callback) {
//        mStartCalendarReference = startCalendarReference;
//        mEndCalendarReference = endCalendarReference;
        mCallback = callback;
        mFragmentManager = fragmentManager;
    }

    public View.OnClickListener getDurationOnClickListener() {
        return mSetDurationOnClickListener;
    }
    private View.OnClickListener mSetDurationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DurationDialogFragment durationDialogFragment = DurationDialogFragment.newInstance();
            durationDialogFragment.setCallback(mCallback);
            durationDialogFragment.show(mFragmentManager, null);
        }

    };
}
