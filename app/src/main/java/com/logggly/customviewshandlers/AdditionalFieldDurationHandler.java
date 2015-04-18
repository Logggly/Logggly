package com.logggly.customviewshandlers;

import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.managers.DurationManager;
import com.logggly.ui.fragments.DurationDialogFragment;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
* Created by Hafiz Waleed Hussain on 2/9/2015.
*/
public class AdditionalFieldDurationHandler extends AbstractAdditionalField{

    private DurationManager mDurationManager;
    private TextView mTextView;
    private Calendar mStartDate;
    private Calendar mEndDate;


    public AdditionalFieldDurationHandler(FragmentManager fragmentManager,
                                          TextView headerTextView,
                                          TextView textView,
                                          Calendar startDate,
                                          Calendar endDate, String tagName) {
        super(fragmentManager, headerTextView);
        mTextView = textView;
        mStartDate = startDate;
        mEndDate = endDate;
        mDurationManager = new DurationManager(fragmentManager,mCallback,tagName);
        mTextView.setOnClickListener(mDurationManager.getDurationOnClickListener());
        setTimeTextView();
    }

    public String getFieldName(){
        return mTextView.getTag().toString();
    }

    public String getFieldType(){
        return mTextView.getContext().getResources().getString(R.string.duration);
    }

    public String getFieldData(){
        return mStartDate.getTimeInMillis()+" "+mEndDate.getTimeInMillis();
    }

    private void setTimeTextView() {
        long diff = mEndDate.getTimeInMillis() - mStartDate.getTimeInMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        mTextView.setText(hours+" : "+minutes+" : "+seconds);
    }

    private DurationDialogFragment.Callback mCallback = new DurationDialogFragment.Callback() {
        @Override
        public void durations(Calendar startTime, Calendar endTime) {
            mStartDate = startTime;
            mEndDate = endTime;
            setTimeTextView();
        }
    };

}
