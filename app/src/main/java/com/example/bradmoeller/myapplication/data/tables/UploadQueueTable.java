package com.example.bradmoeller.myapplication.data.tables;

import android.database.sqlite.SQLiteDatabase;

import com.example.bradmoeller.myapplication.data.DatabaseColumn;
import com.example.bradmoeller.myapplication.data.DatabaseTable;

import java.util.Locale;

import static android.support.v4.util.Preconditions.checkState;

public class UploadQueueTable extends DatabaseTable {

    // Database table
    public static final String TABLE_UPLOADS = "uploads";
    public static final String TABLE_UPLOAD = "upload";

    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_RETRIES = "retries";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ALBUM_ID = "album_id";
    public static final String COLUMN_TAG_ID = "tag_id";
    public static final String COLUMN_DATE_TAKEN = "date_taken";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_WIDTH = "width";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_ORIENTATION = "orientation";
    public static final String COLUMN_IMAGE_ORIGINATION_SOURCE = "origination_source";

    public static final String STATUS_QUEUED = "Q";
    public static final String STATUS_FAILED = "F";
    public static final String STATUS_ERRORED = "E";

    public static final int IMAGE_ORIGINATION_CAMERA = 0;
    public static final int IMAGE_ORIGINATION_ORGANIZER = 1;
    public static final int IMAGE_ORIGINATION_EXTERNAL = 2;

    public UploadQueueTable() {
        super();
        setVersion(1,0,3,0);
        setTableName(TABLE_UPLOAD);
        // v100000
        addColumn(new DatabaseColumn(COLUMN_URI, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_USER_ID, DatabaseColumn.DataType.TEXT).setVersion(1,0,1,0));
        addColumn(new DatabaseColumn(COLUMN_RETRIES, DatabaseColumn.DataType.INTEGER).setVersion(1,0,1,0));
        addColumn(new DatabaseColumn(COLUMN_STATUS, DatabaseColumn.DataType.TEXT).setVersion(1,0,1,0));
        addColumn(new DatabaseColumn(COLUMN_ALBUM_ID, DatabaseColumn.DataType.TEXT).setVersion(1,0,1,0));
        addColumn(new DatabaseColumn(COLUMN_IMAGE_ORIGINATION_SOURCE, DatabaseColumn.DataType.INTEGER).setVersion(1, 0, 0, 0));
        addColumn(new DatabaseColumn(COLUMN_TAG_ID, DatabaseColumn.DataType.TEXT).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_DATE_TAKEN, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_LATITUDE, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_LONGITUDE, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_WIDTH, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_HEIGHT, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
        addColumn(new DatabaseColumn(COLUMN_ORIENTATION, DatabaseColumn.DataType.INTEGER).setVersion(1,0,0,0));
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(generateCreateStatement());
            database.execSQL("CREATE INDEX IF NOT EXISTS file_uri_idx ON " + TABLE_UPLOAD + " (" + COLUMN_URI + ");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        super.onUpgrade(database, oldVersion, newVersion);

        //If old version is less than 10800, completely drop and recreate
        if (oldVersion < 10800){
            database.execSQL("DROP TABLE " + TABLE_UPLOAD + ";");
            database.execSQL(generateCreateStatement());
            database.execSQL("CREATE INDEX IF NOT EXISTS file_uri_idx ON " + TABLE_UPLOAD + " (" + COLUMN_URI + ");");
        }
    }

    @Override
    public String generateCreateStatement() {
        checkState(mColumns.size() > 0, "Table columns not defined");
        return String.format(Locale.getDefault(), "CREATE TABLE %s (", mTableName) + generateColumnsClause() + ", CONSTRAINT user_uri_unique UNIQUE (" + COLUMN_URI + "," + COLUMN_USER_ID + "));";
    }
}
