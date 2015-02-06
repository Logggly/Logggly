package com.logggly.managers;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import com.logggly.databases.DatabaseContract;

/**
 * Created by Hafiz Waleed Hussain on 2/5/2015.
 */
public class TagAdapterManager {

    private SimpleCursorAdapter mTagsAdapter;
    private static final int TAGS_LOADER = 2;
    private FragmentActivity mFragmentActivity;
    private boolean isFilter = false;
    private String filterString = "";

    public TagAdapterManager(FragmentActivity fragmentActivity) {
        mFragmentActivity = fragmentActivity;
        mTagsAdapter = new SimpleCursorAdapter(fragmentActivity,
                android.R.layout.simple_spinner_item,
                null,
                new String[]{
                        DatabaseContract.Tags.COLUMN_NAME},
                new int[]{android.R.id.text1});
    }

    public void initLoader(){
        mFragmentActivity
                .getSupportLoaderManager()
                .initLoader(TAGS_LOADER, null, mCursorLoaderCallbacks);
    }

    public void destroyLoader(){
        mFragmentActivity.getSupportLoaderManager().destroyLoader(TAGS_LOADER);
    }

    public void restartLoaderWithFilter(String string){
        if( string!=null && string.isEmpty()){
            isFilter = false;
            filterString = "";
        }else{
            isFilter = true;
            filterString = string;
            mFragmentActivity.getSupportLoaderManager().restartLoader(TAGS_LOADER,null,mCursorLoaderCallbacks);
        }
    }

    public SimpleCursorAdapter getAdapter() {
        return mTagsAdapter;
    }

   private LoaderManager.LoaderCallbacks<Cursor> mCursorLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
       @Override
       public Loader<Cursor> onCreateLoader(int id, Bundle args) {
           if(isFilter == false){
               return new CursorLoader(mFragmentActivity,
                       DatabaseContract.Tags.CONTENT_URI,
                       null,null,null,DatabaseContract.Tags.COLUMN_NAME+ " ASC");
           }else{
               return new CursorLoader(mFragmentActivity,
                       DatabaseContract.Tags.buildUriForIncompleteTagSearch(filterString),
                       null,null,null,DatabaseContract.Tags.COLUMN_NAME+ " ASC");
           }
       }

       @Override
       public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
           mTagsAdapter.swapCursor(data);
           mTagsAdapter.notifyDataSetChanged();
       }

       @Override
       public void onLoaderReset(Loader<Cursor> loader) {
           mTagsAdapter.swapCursor(null);
       }
   };



}
