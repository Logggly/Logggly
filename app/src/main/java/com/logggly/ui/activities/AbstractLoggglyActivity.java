package com.logggly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.logggly.R;
import com.logggly.databases.DatabaseContract;
import com.logggly.ui.fragments.ImportCSVDialogFragment;
import com.logggly.ui.fragments.TagsListDialogFragment;
import com.logggly.utilities.CSVUtility;

/**
 * Created by Hafiz Waleed Hussain on 1/28/2015.
 */
public abstract class AbstractLoggglyActivity extends ActionBarActivity{

    public static Context BASE_ACTIVITY_CONTEXT;

    private static final int GET_ACTIVITY = 10;

    protected ActionBar mActionBar;
    private CSVUtility csvUtility;

//    private VoiceUtility mVoiceUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BASE_ACTIVITY_CONTEXT = this;
        mActionBar = getSupportActionBar();
        csvUtility = new CSVUtility();
//        mVoiceUtility = VoiceUtility.getInstance(this);
//        mVoiceUtility.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mVoiceUtility.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Menu_export_csv:
                exportCSV();
                return true;
            case R.id.Menu_import_csv:
                importCSV();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == RESULT_OK){
            if( resultCode == GET_ACTIVITY){
                Toast.makeText(this,data.getData().getPath(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importCSV(){
        ImportCSVDialogFragment importCSVDialogFragment = ImportCSVDialogFragment.newInstance();
        importCSVDialogFragment.show(getSupportFragmentManager(),null);
    }

    private void exportCSV(){
        TagsListDialogFragment tagsListDialogFragment = new TagsListDialogFragment();
        tagsListDialogFragment.setCallback(new TagsListDialogFragment.Callback() {
            @Override
            public void selectedTagName(String name) {
                if(name.equalsIgnoreCase("all")){
                    Toast.makeText(BASE_ACTIVITY_CONTEXT,"\"All\" tag not supported",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                    Cursor cursor = getContentResolver().query(DatabaseContract.Tasks.buildUriAgainstTagName(name),
                            null, null, null, null);
                    if(cursor.getCount() <= 0){
                        Toast.makeText(BASE_ACTIVITY_CONTEXT,"No data available against " +
                                        "tag \""+name+"\"",
                                Toast.LENGTH_SHORT).show();
                        cursor.close();
                        return;
                    }
                csvUtility.exportCSV(name, cursor);


            }
        });
        tagsListDialogFragment.show(getSupportFragmentManager(),null);
    }
}
