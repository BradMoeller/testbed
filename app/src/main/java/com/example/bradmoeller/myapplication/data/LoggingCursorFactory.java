package com.example.bradmoeller.myapplication.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.support.compat.BuildConfig;
import android.util.Log;


/**
 * Created by gcahill on 9/19/16.
 */
public class LoggingCursorFactory implements SQLiteDatabase.CursorFactory {

    private static final String TAG = "SQL_LOG";

    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
        if(BuildConfig.DEBUG){
            Log.d(TAG, query.toString());
        }
        return new SQLiteCursor( masterQuery, editTable, query);
    }

}
