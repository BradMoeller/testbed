package com.example.bradmoeller.myapplication.data;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseObject {

    void onCreate(SQLiteDatabase database);
    void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);

}
