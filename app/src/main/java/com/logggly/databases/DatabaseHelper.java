package com.logggly.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class create Databases and also give us the helper methods. Which require to
 * work with databases.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    /** Database name constant*/
    public static final String DATABASE_NAME = "Loggggy.db";
    /** Database version constant*/
    public static final int DATABASE_VERSION = 1;

    /**
     *  Simple constructor which initialize a Database Helper Object.
     * @param context Context of the class where object use.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Tasks.SQL_CREATE_TABLE);
        db.execSQL(DatabaseContract.Tags.SQL_CREATE_TABLE);

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
