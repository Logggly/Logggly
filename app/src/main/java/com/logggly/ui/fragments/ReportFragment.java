package com.logggly.ui.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.logggly.R;
import com.logggly.adapters.TasksAdapter;
import com.logggly.databases.DatabaseContract;
import com.logggly.managers.TagAdapterManager;

/**
 * Created by Hafiz Waleed Hussain on 1/28/2015.
 */
public class ReportFragment extends AbstractLoggglyFragment implements
        LoaderManager.LoaderCallbacks<Cursor>{


    public static final ReportFragment newInstance() {
        ReportFragment reportFragment = new ReportFragment();
        return reportFragment;
    }

    private static final int TASK_LOADER = 1;

    private Spinner mTagsSpinner;
    private ListView mTasksListView;
    private TasksAdapter mTasksAdapter;
    private Button mDeleteAllWithSelectedTagButton;
    private TagAdapterManager mTagAdapterManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(getActivity(),null);
        mTagAdapterManager = new TagAdapterManager(getActivity());
//        mTagsAdapter = new SimpleCursorAdapter(getActivity(),
//                android.R.layout.simple_spinner_item,
//                null,
//                new String[]{DatabaseContract.Tags.COLUMN_NAME},
//                new int[]{android.R.id.text1});

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_fragment,container,false);
        mTagsSpinner = (Spinner) view.findViewById(R.id.FragmentReport_tags_spinner);
        mTagsSpinner.setAdapter(mTagAdapterManager.getAdapter());
        mTagsSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mTasksListView = (ListView) view.findViewById(R.id.FragmentReport_task_listview);
        mTasksListView.setAdapter(mTasksAdapter);
        mTasksListView.setOnItemClickListener(mOnItemClickListener);
        mDeleteAllWithSelectedTagButton = (Button) view.findViewById(R.id.FragmentReport_delete_all_button);
        mDeleteAllWithSelectedTagButton.setOnClickListener(mDeleteAllWithTagOnClickListener);
        mTagAdapterManager.initLoader();
        getLoaderManager().initLoader(TASK_LOADER,null,this);
        return view;
    }


    private View.OnClickListener mDeleteAllWithTagOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mTagName.equals("All")){
                Toast.makeText(getActivity(), "This tag is not delete able", Toast.LENGTH_SHORT).show();
            }else{
                getActivity().getContentResolver().delete(
                        DatabaseContract.Tasks.buildUriAgainstTagName(mTagName),null,null);
            }
        }
    };

    private boolean filter = false;
    private String mTagName = "All";

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case TASK_LOADER:
                if(filter){
                    return new CursorLoader(getActivity(),
                            DatabaseContract.Tasks.buildUriAgainstTagName(mTagName),
                            null, null, null,
                            DatabaseContract.Tasks.COLUMN_DATE_TIME + " DESC");
                }else {
                    return new CursorLoader(getActivity(),
                            DatabaseContract.Tasks.CONTENT_URI,
                            null, null, null,
                            DatabaseContract.Tasks.COLUMN_DATE_TIME + " DESC");
                }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case TASK_LOADER:
                mTasksAdapter.swapCursor(data);
                mTasksListView.setAdapter(mTasksAdapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case TASK_LOADER:
                mTasksAdapter.swapCursor(null);
                break;
        }
    }

    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
            mTagName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Tags.COLUMN_NAME));
            filter = !mTagName.equals("All");
            getLoaderManager().restartLoader(TASK_LOADER,null,ReportFragment.this);
//            Toast.makeText(getActivity(),tagSelectedId+"",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.MainActivity_container_framelayout
                    , PagerReportDetailFragment.
                    newInstance(position))
                    .addToBackStack(null)
                    .commit();


//            try {
//                TaskModel taskModel = new TaskModel((Cursor) parent.getAdapter().getItem(position));
//                ReportDetailFragment reportDetailFragment = ReportDetailFragment.newInstance(taskModel);
////                reportDetailFragment.show(getActivity().getFragmentManager(), null);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
    };
}
