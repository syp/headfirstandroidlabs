package com.example.headfirstlabs8timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stephen on 13-7-1.
 */
public class TimeTrackerDBHelper {
    public static final int TIMETRACKER_DBVER = 2;
    public static final String TIMETRACKER_DBNAME = "timetracker.db";
    public static final String TIMETRACKER_TABLENAME = "timerecords";
    public static final String TIMETRACKER_COLNAME_TIME = "time";
    public static final String TIMETRACKER_COLNAME_NOTES = "notes";

    private TimeTrackerOpenHelper openHelper;
    private SQLiteDatabase db;

    public TimeTrackerDBHelper(Context context){
        openHelper = new TimeTrackerOpenHelper(context);
        db = openHelper.getWritableDatabase();
    }

    public Cursor getAllTimeRecords(){
        return db.rawQuery("SELECT * FROM "+TIMETRACKER_TABLENAME, null);
    }

    public void addTimeRecord(String time, String notes){
        ContentValues content = new ContentValues();
        content.put(TIMETRACKER_COLNAME_TIME, time);
        content.put(TIMETRACKER_COLNAME_NOTES, notes);
        db.insert(TIMETRACKER_TABLENAME, null, content);
    }

    private class TimeTrackerOpenHelper extends SQLiteOpenHelper{
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TIMETRACKER_TABLENAME+
                    " (_id integer primary key, "+TIMETRACKER_COLNAME_TIME+" text, "+TIMETRACKER_COLNAME_NOTES+" text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TIMETRACKER_TABLENAME);
            onCreate(sqLiteDatabase);
        }

        public TimeTrackerOpenHelper(Context context) {
            super(context, TIMETRACKER_DBNAME, null, TIMETRACKER_DBVER);
        }
    }


}
