package com.logggly.databases;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hafiz Waleed Hussain on 12/22/2014.
 * This class having all meta data about tables.
 */
public class DatabaseContract {

    /** Authority string constant*/
    public static final String  AUTHORITY = "com.logggly";
    /** BaseUri Uri constant. */
    public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);


    /**
     * Contacts having all of our meta data about 'Contacts' database table.
     */
    public static final class Tasks implements BaseColumns{
        /** Table path constant*/
        public static final String PATH = "tasks";
        public static final String PATH_DELETE = "tasks_delete";
        /** Table name constant */
        public static final String TABLE_NAME = "Tasks";
        /** Table column name constant*/
        public static final String COLUMN_TAG = "tag";
        /** Table column name constant*/
        public static final String COLUMN_NOTES = "notes";
        /** Table column name constant*/
        public static final String COLUMN_LATITUDE = "latitude";
        /** Table column name constant*/
        public static final String COLUMN_LONGITUDE = "longitude";
        /** Table column name constant*/
        public static final String COLUMN_LOCATION_NAME = "location_name";
        /** Table column name constant*/
        public static final String COLUMN_CREATED_AT = "created_at";
        /** Table column name constant*/
        public static final String COLUMN_DATE_TIME = "date_time";

        /** Mime types*/
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/"+AUTHORITY+"/"+PATH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/"+AUTHORITY+"/"+PATH;

        /** ContentUri is use in ContentProvider, to get data from database table Contacts*/
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().
                                                appendPath(PATH).
                                                build();

        public static Uri buildUriAgainstTagName(String tagName){
            return CONTENT_URI.buildUpon().appendPath(tagName).build();
        }

        public static Uri buildUriAgainstDeleteId(String id){
            Uri uri = BASE_URI.buildUpon().appendPath(PATH_DELETE).build();
            return ContentUris.withAppendedId(uri, Long.parseLong(id));
        }

        public static String getTagNameFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }


        /** Table creation query */
        public static final String SQL_CREATE_TABLE = String.format("CREATE TABLE " +
                "%s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL COLLATE NOCASE," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s FLOAT," +
                "%s FLOAT," +
                "%s DATETIME," +
                "%s DATETIME DEFAULT (datetime('now','localtime'))" +
                ");",
                TABLE_NAME,_ID,
                COLUMN_TAG,
                COLUMN_NOTES,
                COLUMN_LOCATION_NAME,
                COLUMN_LATITUDE,
                COLUMN_LONGITUDE,
                COLUMN_DATE_TIME,
                COLUMN_CREATED_AT);

    }

    /**
     * ContactCallHistory having all of our calling history which handle by our App.
     */
    public static final class Tags implements BaseColumns{

        /** Table path constant*/
        public static final String PATH = "tags";
        /** Table name constant */
        public static final String TABLE_NAME = "Tags";
        /** Table column name constant*/
        public static final String COLUMN_NAME = "name";

        /** Mime types*/
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/"+AUTHORITY+"/"+PATH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/"+AUTHORITY+"/"+PATH;


        /** ContentUri is use in ContentProvider, to get data from database table
         * ContactsCallHistory*/
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().
                appendPath(PATH).
                build();

        public static Uri buildUriForSearchTag(String tagName){
            return CONTENT_URI.buildUpon().appendPath(tagName).build();
        }

        public static String getSearchNameFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static Uri buildUriForIncompleteTagSearch(String incompleteTagName){
            return CONTENT_URI.buildUpon().appendPath("incomplete_search").appendPath(incompleteTagName).build();
        }

        public static String getIncompleteTagSearchNameFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }


        /** Table creation query */
        public static final String SQL_CREATE_TABLE = String.format("CREATE TABLE " +
                "%s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT NOT NULL" +
                ");",TABLE_NAME,_ID, COLUMN_NAME);

    }
}
