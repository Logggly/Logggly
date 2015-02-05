package com.logggly.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.logggly.R;
import com.logggly.databases.DatabaseContract;
import com.logggly.utilities.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Hafiz Waleed Hussain on 1/28/2015.
 */
public class TasksAdapter extends SimpleCursorAdapter{

    private final LayoutInflater mLayoutInflater;

    public TasksAdapter(Context context, Cursor c) {
        super(context,
                R.layout.row_fragment_report,
                c,
                new String[]{
                        DatabaseContract.Tasks._ID,
                        DatabaseContract.Tasks.COLUMN_TAG,
                        DatabaseContract.Tasks.COLUMN_DATE_TIME,
                        DatabaseContract.Tasks.COLUMN_DATE_TIME
                },
                new int[]{
                        R.id.RowFragmentReport_id_textview,
                        R.id.RowFragmentReport_tag_textview,
                        R.id.RowFragmentReport_date_textview,
                        R.id.RowFragmentReport_time_textview
                },
                0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        try {
            Calendar calendar = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            java.util.Date dt = null; //replace 4 with the column index
            dt = sdf.parse(cursor.getString(cursor.getColumnIndex(DatabaseContract.Tasks.COLUMN_DATE_TIME)));
            calendar.setTime(dt);

            TextView mDateTextView = (TextView) view.findViewById(R.id.RowFragmentReport_date_textview);
            mDateTextView.setText(DateTimeFormatter.dateFormatter(calendar));

            TextView mTimeTextView = (TextView) view.findViewById(R.id.RowFragmentReport_time_textview);
            mTimeTextView.setText(DateTimeFormatter.timeFormatter(calendar));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
