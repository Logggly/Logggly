package com.logggly;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.logggly.databases.DatabaseContract;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Hafiz Waleed Hussain on 1/28/2015.
 */
public class LoggglyApplication extends Application {

    public static Context CONTEXT;
    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        Cursor cursor = getContentResolver().query(DatabaseContract.Tags.buildUriForSearchTag("All"), null, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.Tags.COLUMN_NAME, "All");
            getContentResolver().insert(DatabaseContract.Tags.CONTENT_URI, contentValues);

            contentValues.put(DatabaseContract.Tags.COLUMN_NAME, "Null");
            getContentResolver().insert(DatabaseContract.Tags.CONTENT_URI, contentValues);

        }
    }

}
