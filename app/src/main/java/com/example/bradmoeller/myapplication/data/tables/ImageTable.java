package com.example.bradmoeller.myapplication.data.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bradmoeller.myapplication.BuildConfig;
import com.example.bradmoeller.myapplication.data.DatabaseColumn;
import com.example.bradmoeller.myapplication.data.DatabaseTable;

public class ImageTable extends DatabaseTable {

    public static final String TABLE_IMAGES = "images";
    public static final String TABLE_IMAGE = "image";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FOREVER_IMAGE_ID = "forever_image_id";
    public static final String COLUMN_FILE_URI = "file_uri";
    public static final String COLUMN_SHA1_HASH = "sha1_hash";

    public ImageTable() {
        super();
        setVersion(1,0,1,0);
        setTableName(TABLE_IMAGE);

        addColumn(new DatabaseColumn(COLUMN_USER_ID, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_FOREVER_IMAGE_ID, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_FILE_URI, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_SHA1_HASH, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(generateCreateStatement());
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS forever_image_id_file_uri_uniqueness on " + TABLE_IMAGE + " (" + COLUMN_FOREVER_IMAGE_ID + "," + COLUMN_FILE_URI + ");");
            database.execSQL("CREATE INDEX IF NOT EXISTS file_uri_idx ON " + TABLE_IMAGE + " (" + COLUMN_FILE_URI + ");");
        }
        catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG) {
                Log.w("DBCreate", e.toString());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        super.onUpgrade(database, oldVersion, newVersion);

        //If old version is less than 10800, completely drop and recreate
        if (oldVersion < 10800){
            database.execSQL("DROP TABLE " + TABLE_IMAGE + ";");
            database.execSQL(generateCreateStatement());
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS forever_image_id_file_uri_uniqueness on " + TABLE_IMAGE + " (" + COLUMN_FOREVER_IMAGE_ID + "," + COLUMN_FILE_URI + ");");
            database.execSQL("CREATE INDEX IF NOT EXISTS file_uri_idx ON " + TABLE_IMAGE + " (" + COLUMN_FILE_URI + ");");
        }

    }

}
