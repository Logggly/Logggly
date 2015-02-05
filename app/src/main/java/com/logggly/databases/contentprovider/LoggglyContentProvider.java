package com.logggly.databases.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.logggly.databases.DatabaseHelper;

import static com.logggly.databases.DatabaseContract.AUTHORITY;
import static com.logggly.databases.DatabaseContract.Tags;
import static com.logggly.databases.DatabaseContract.Tasks;

public class LoggglyContentProvider extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;


    private static final int TASKS = 0x1;
    private static final int TASKS_AGAINST_TAGS = 0x2;
    private static final int TAGS = 0x3;
    private static final int TAG_SEARCH = 0x4;
    private static final int TASKS_AGAINST_ID = 0x5;
    private static final int SEARCH_TAGS = 0x6;

    private static final UriMatcher URI_MATCHER;


    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Tasks.PATH, TASKS);
        URI_MATCHER.addURI(AUTHORITY, Tasks.PATH + "/*", TASKS_AGAINST_TAGS);
        URI_MATCHER.addURI(AUTHORITY, Tasks.PATH_DELETE + "/#", TASKS_AGAINST_ID);
        URI_MATCHER.addURI(AUTHORITY, Tags.PATH, TAGS);
        URI_MATCHER.addURI(AUTHORITY, Tags.PATH + "/*", TAG_SEARCH);
        URI_MATCHER.addURI(AUTHORITY, Tags.PATH + "/*/*", SEARCH_TAGS);

    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return mDatabaseHelper != null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        switch (getMatcherCode(uri)){
            case TASKS:
                return sqLiteDatabase.delete(Tasks.TABLE_NAME,null,null);
            case TASKS_AGAINST_TAGS:
                String tag = Tasks.getTagNameFromUri(uri).toLowerCase();
                sqLiteDatabase.delete(Tasks.TABLE_NAME,Tasks.COLUMN_TAG+"=?",
                        new String[]{tag});
                getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver().notifyChange(Tags.CONTENT_URI, null);
                return sqLiteDatabase.delete(Tags.TABLE_NAME, Tags.COLUMN_NAME+"=?",
                        new String[]{tag});
            case TASKS_AGAINST_ID:
                String id = ContentUris.parseId(uri)+"";
                int intId = sqLiteDatabase.delete(Tasks.TABLE_NAME, Tasks._ID+"=?", new String[]{id});
            getContext().getContentResolver().notifyChange(Tasks.CONTENT_URI, null);
            return intId;
            default:
                return 0;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (getMatcherCode(uri)) {
            case TASKS:
                return Tasks.CONTENT_TYPE;
            case TASKS_AGAINST_TAGS:
                return Tasks.CONTENT_TYPE;
            case TAGS:
                return Tags.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri newUriWithInsertionId;
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        long id;
        switch (getMatcherCode(uri)) {
            case TASKS:
                id = sqLiteDatabase.insert(Tasks.TABLE_NAME, null, values);
                newUriWithInsertionId = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(newUriWithInsertionId,null);
                break;
            case TAGS:
                id = sqLiteDatabase.insert(Tags.TABLE_NAME, null, values);
                newUriWithInsertionId = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(newUriWithInsertionId, null);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return newUriWithInsertionId;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = getMatcherCode(uri);
        switch (getMatcherCode(uri)) {

            case TASKS_AGAINST_TAGS:
                cursor = sqLiteDatabase.query(
                        Tasks.TABLE_NAME,
                        projection,
                        Tasks.COLUMN_TAG + " =? COLLATE NOCASE",
                        new String[]{Tasks.getTagNameFromUri(uri).toLowerCase()}, null, null, sortOrder);
                break;
            case TASKS:
                cursor = sqLiteDatabase.query(Tasks.TABLE_NAME, null, null, null, null, null, sortOrder);
                break;
            case TAGS:
                cursor = sqLiteDatabase.query(Tags.TABLE_NAME, null, null, null, null, null,sortOrder);
                break;
            case TAG_SEARCH:
                cursor = sqLiteDatabase.query(
                        Tags.TABLE_NAME,
                        projection,
                        Tags.COLUMN_NAME+ " =?",
                        new String[]{Tags.getSearchNameFromUri(uri)}, null, null, sortOrder);

                break;
            case SEARCH_TAGS:
                cursor = sqLiteDatabase.query(
                        Tags.TABLE_NAME,
                        projection,
                        Tags.COLUMN_NAME+ " LIKE ?",
                        new String[]{"%"+Tags.getIncompleteTagSearchNameFromUri(uri)+"%"}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getReadableDatabase();
        int id;
        switch (getMatcherCode(uri)) {
            case TASKS:
               id = sqLiteDatabase.update(Tasks.TABLE_NAME,values,"_id=?",
                        new String[]{values.getAsLong(Tasks._ID)+""});
                getContext().getContentResolver().notifyChange(Tasks.CONTENT_URI, null);
            break;
            default:
                return -1;
        }
        return id;
    }

    private int getMatcherCode(Uri uri) {
        return URI_MATCHER.match(uri);
    }
}
