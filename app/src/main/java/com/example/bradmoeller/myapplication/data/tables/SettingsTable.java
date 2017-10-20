package com.example.bradmoeller.myapplication.data.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.bradmoeller.myapplication.data.DatabaseColumn;
import com.example.bradmoeller.myapplication.data.DatabaseTable;

public class SettingsTable extends DatabaseTable {

    // Database table
    public static final String TABLE_SETTINGS = "settings";

    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE_DATA_TYPE = "value_datatype";
    public static final String COLUMN_VALUE_STRING = "value_string";
    public static final String COLUMN_VALUE_NUMBER = "value_number";
    public static final String COLUMN_VALUE_BOOLEAN = "value_boolean";

    public static final String COLUMN_DATA_TYPE_STRING = "string";
    public static final String COLUMN_DATA_TYPE_NUMBER = "number";
    public static final String COLUMN_DATA_TYPE_BOOLEAN = "boolean";

    public SettingsTable() {

        super();
        setVersion(1,0,0,0);
        setTableName(TABLE_SETTINGS);
        // v100000
        addColumn(new DatabaseColumn(COLUMN_KEY, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_VALUE_DATA_TYPE, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_VALUE_STRING, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_VALUE_NUMBER, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_VALUE_BOOLEAN, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(generateCreateStatement());
        try {
            database.execSQL("CREATE UNIQUE INDEX settings_key_idx on " + TABLE_SETTINGS + " (" + COLUMN_KEY + ");");

            database.beginTransaction();

            ContentValues values = new ContentValues();

            values.clear();
            values.put("key","mediastore_last_query_time");
            values.put("value_datatype","number");
            values.putNull("value_string");
            values.putNull("value_boolean");
            values.put("value_number", (System.currentTimeMillis() / 1000));
            database.insert(TABLE_SETTINGS, null, values);

            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
