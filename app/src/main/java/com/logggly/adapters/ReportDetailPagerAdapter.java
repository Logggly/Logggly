package com.logggly.adapters;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.logggly.models.TaskModel;
import com.logggly.ui.fragments.ReportDetailFragment;

import java.text.ParseException;

/**
 * Created by Hafiz Waleed Hussain on 2/5/2015.
 */
public class ReportDetailPagerAdapter extends FragmentStatePagerAdapter{

    private Cursor mCursor;
    public ReportDetailPagerAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        mCursor = cursor;

    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        mCursor.moveToPosition(position);
        try {
            TaskModel taskModel = new TaskModel(mCursor);
            ReportDetailFragment reportDetailFragment = ReportDetailFragment.newInstance(taskModel);
            return reportDetailFragment;
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

}
