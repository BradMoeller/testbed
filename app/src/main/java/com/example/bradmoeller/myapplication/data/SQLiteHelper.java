package com.example.bradmoeller.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bradmoeller.myapplication.data.tables.ImageTable;
import com.example.bradmoeller.myapplication.data.tables.SettingsTable;
import com.example.bradmoeller.myapplication.data.tables.UploadQueueTable;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myapplication.db";
    private static final int DATABASE_MAJOR_VERSION = 1;
    private static final int DATABASE_MINOR_VERSION = 0;
    private static final int DATABASE_PATCH_VERSION = 8;
    private static final int DATABASE_BUILD_VERSION = 0;

    private final List<DatabaseObject> mTables = new ArrayList<>();

    private static SQLiteHelper instance;

    public static synchronized SQLiteHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLiteHelper(context, new LoggingCursorFactory());
        }
        return instance;
    }

    private SQLiteHelper(Context context, SQLiteDatabase.CursorFactory cursorFactory) {
        super(context, DATABASE_NAME, cursorFactory, getVersionCode());
        mTables.add(new UploadQueueTable());
        mTables.add(new ImageTable());
        mTables.add(new SettingsTable());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for (DatabaseObject table : mTables) {
            table.onCreate(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        switch (oldVersion){
            case 10500:
                //Drop the 'deviceimage' table from the database
                database.execSQL("DROP TABLE IF EXISTS 'deviceimage'");
        }

        for (DatabaseObject table : mTables) {
            table.onUpgrade(database, oldVersion, newVersion);
        }
    }

    public static int getVersionCode() {
        return (DATABASE_MAJOR_VERSION * 10000) + (DATABASE_MINOR_VERSION * 1000) + (DATABASE_PATCH_VERSION * 100) + DATABASE_BUILD_VERSION;
    }

    public String getVersionName() {
        return String.format("%s.%s.%s.%s", String.valueOf(DATABASE_MAJOR_VERSION), String.valueOf(DATABASE_MINOR_VERSION), String.valueOf(DATABASE_PATCH_VERSION), String.valueOf(DATABASE_BUILD_VERSION));
    }

}
