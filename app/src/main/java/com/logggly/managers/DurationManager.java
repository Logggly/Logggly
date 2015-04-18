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
    private String mTagName;

    public DurationManager(FragmentManager fragmentManager,
                           DurationDialogFragment.Callback callback, String tagName) {
//        mStartCalendarReference = startCalendarReference;
//        mEndCalendarReference = endCalendarReference;
        mCallback = callback;
        mFragmentManager = fragmentManager;
        mTagName = tagName;
    }

    public View.OnClickListener getDurationOnClickListener() {
        return mSetDurationOnClickListener;
    }
    private View.OnClickListener mSetDurationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DurationDialogFragment durationDialogFragment = DurationDialogFragment.newInstance(mTagName);
            durationDialogFragment.setCallback(mCallback);
            durationDialogFragment.show(mFragmentManager, null);
        }

    };
}
