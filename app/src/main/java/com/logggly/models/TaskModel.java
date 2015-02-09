package com.logggly.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.logggly.databases.DatabaseContract;
import com.logggly.utilities.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Hafiz Waleed Hussain on 1/31/2015.
 */
public class TaskModel implements Serializable{


    public TaskModel(Cursor cursor) throws ParseException {
        mId = cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks._ID));
        mTag = cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_TAG));
        mLocation = cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_LOCATION_NAME));
        mNotes = cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_NOTES));
        mAdditionalFields = cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_ADDITIONAL_FIELDS));
//        mLatitude = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_LATITUDE));
//        mLongitude = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_LONGITUDE));
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        java.util.Date dt = null; //replace 4 with the column index
        dt = sdf.parse(cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_DATE_TIME)));
        calendar.setTime(dt);
        mCalendar = calendar;
    }

    private String mId;
    private String mTag;
    private String mDate;
    private String mTime;
    private String mLocation;
    private String mNotes;
    private Calendar mCalendar;
    private String mAdditionalFields;

    public JSONArray getAdditionalFields() {
        try {
            return new JSONArray(mAdditionalFields);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAdditionalFields(String additionalFields) {
        mAdditionalFields = additionalFields;
    }

    public String getId() {
        return mId;
    }

    public String getTag() {
        return mTag;
    }

    public String getDate() {
        mDate = DateTimeFormatter.dateFormatter(mCalendar);
        return mDate;
    }

    public String getTime() {
        mTime = DateTimeFormatter.timeFormatter(mCalendar);
        return mTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseContract.Tasks.COLUMN_TAG, mTag);
//        contentValues.put(DatabaseContract.Tasks.COLUMN_LATITUDE,mLatitude);
//        contentValues.put(DatabaseContract.Tasks.COLUMN_LONGITUDE,mLongitude);
//        contentValues.put(DatabaseContract.Tasks.COLUMN_NOTES, mNotes);
//        contentValues.put(DatabaseContract.Tasks.COLUMN_LOCATION_NAME, mLocation);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(mCalendar.getTimeInMillis());
        contentValues.put(DatabaseContract.Tasks.COLUMN_DATE_TIME, dateFormat.format(date));
        contentValues.put(DatabaseContract.Tasks.COLUMN_ADDITIONAL_FIELDS, mAdditionalFields);
        contentValues.put(DatabaseContract.Tasks._ID,mId);
        contentValues.put(DatabaseContract.Tasks.COLUMN_NOTES, mNotes);
        return contentValues;
    }
}
