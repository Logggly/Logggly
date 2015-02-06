package com.logggly.ui.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logggly.R;
import com.logggly.adapters.ReportDetailPagerAdapter;
import com.logggly.databases.DatabaseContract;

public class PagerReportDetailFragment extends AbstractLoggglyFragment implements LoaderManager.
        LoaderCallbacks<Cursor>{

    private int mPosition;
    private ViewPager mViewPager;
    private ReportDetailPagerAdapter mReportDetailPagerAdapter;

    public static PagerReportDetailFragment newInstance(int position) {
        PagerReportDetailFragment fragment = new PagerReportDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PagerReportDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt("position",1);
        }
        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_pager_report,container,false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                DatabaseContract.Tasks.CONTENT_URI,
                null, null, null,
                DatabaseContract.Tasks.COLUMN_TAG+","+DatabaseContract.Tasks.COLUMN_DATE_TIME + " ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToPosition(mPosition);
        if(mReportDetailPagerAdapter == null){
            mReportDetailPagerAdapter = new ReportDetailPagerAdapter(getActivity()
                    .getSupportFragmentManager(),data);
        }else{
            mReportDetailPagerAdapter.swapCursor(data);
        }
        mViewPager.setAdapter(mReportDetailPagerAdapter);
        mViewPager.setCurrentItem(mPosition);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(mReportDetailPagerAdapter != null){
            mReportDetailPagerAdapter.swapCursor(null);
        }
    }
}
