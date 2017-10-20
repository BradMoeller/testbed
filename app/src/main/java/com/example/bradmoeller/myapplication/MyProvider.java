package com.example.bradmoeller.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.bradmoeller.myapplication.data.SQLiteHelper;
import com.example.bradmoeller.myapplication.data.tables.ImageTable;
import com.example.bradmoeller.myapplication.data.tables.SettingsTable;
import com.example.bradmoeller.myapplication.data.tables.UploadQueueTable;

import java.io.FileNotFoundException;

import static com.example.bradmoeller.myapplication.data.tables.ImageTable.TABLE_IMAGE;
import static com.example.bradmoeller.myapplication.data.tables.ImageTable.TABLE_IMAGES;
import static com.example.bradmoeller.myapplication.data.tables.SettingsTable.TABLE_SETTINGS;
import static com.example.bradmoeller.myapplication.data.tables.UploadQueueTable.TABLE_UPLOAD;
import static com.example.bradmoeller.myapplication.data.tables.UploadQueueTable.TABLE_UPLOADS;

/**
 * Created by bradmoeller on 9/27/17.
 */

public class MyProvider extends ContentProvider {
    private static final String TAG = MyProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.example.bradmoeller.myapplication.provider";
    private static final int ALL_UPLOADS = 1;
    private static final int UPLOAD_ID = 2;
    private static final int ALL_IMAGES = 3;
    private static final int IMAGE_ID = 4;
    private static final int ALL_SETTINGS = 5;
    private static final int SETTINGS_ID = 6;

    private static final String UPLOADS = "uploads";
    private static final String IMAGES = "images";
    private static final String SETTINGS = "settings";
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final Uri IMAGE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_IMAGES);

    private SQLiteHelper mDatabase;

    static {
        mUriMatcher.addURI(AUTHORITY, TABLE_UPLOADS, ALL_UPLOADS);
        mUriMatcher.addURI(AUTHORITY, TABLE_UPLOADS + "/*", UPLOAD_ID);
        mUriMatcher.addURI(AUTHORITY, TABLE_IMAGES, ALL_IMAGES);
        mUriMatcher.addURI(AUTHORITY, TABLE_IMAGES + "/*", IMAGE_ID);
        mUriMatcher.addURI(AUTHORITY, TABLE_SETTINGS, ALL_SETTINGS);
        mUriMatcher.addURI(AUTHORITY, TABLE_SETTINGS + "/*", SETTINGS_ID);
    }


    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate()");
        Context context = getContext();
        if (context != null) {
            mDatabase = SQLiteHelper.getInstance(context);
            return true;
        }
        else{
            Log.e(TAG, "Failed to initialize. Context was null");
            return false;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
//        checkColumns(projection);

        // Set the table


        Log.w(TAG, "<!********** Uri: " + uri.toString() + " *********!>");

        int uriType = mUriMatcher.match(uri);
//        checkColumns(projection, uriType);
        Log.w(TAG, "<!********** UriType: " + String.valueOf(uriType) + " *********!>");
        switch (uriType) {
            case ALL_UPLOADS:
                queryBuilder.setTables(TABLE_UPLOAD);
                break;
            case UPLOAD_ID:
                // adding the ID to the original query
                queryBuilder.setTables(TABLE_UPLOAD);
                queryBuilder.appendWhere(UploadQueueTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case ALL_IMAGES:
                queryBuilder.setTables(TABLE_IMAGE);
                break;
            case IMAGE_ID:
                queryBuilder.setTables(TABLE_IMAGE);
                queryBuilder.appendWhere(ImageTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case ALL_SETTINGS:
                queryBuilder.setTables(SettingsTable.TABLE_SETTINGS);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDatabase.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = queryBuilder.query(db, projection, selection,
                    selectionArgs, null, null, sortOrder);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        String path = uri.toString();

        if (path.contains("images")) {
            return ("image/jpg");
        }

        return (null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri response = null;
        try {
            long id = 0;
            int uriType = mUriMatcher.match(uri);
            SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
            if (sqlDB != null) {
                switch (uriType) {
                    case ALL_UPLOADS:
                        id = sqlDB.insertOrThrow(TABLE_UPLOAD, null, values);
                        response = Uri.parse(UPLOADS + "/" + id);
                        break;
                    case ALL_IMAGES:
                        id = sqlDB.insertOrThrow(TABLE_IMAGE, null, values);
                        response = Uri.parse(IMAGES + "/" + id);
                        break;
                    case ALL_SETTINGS:
                        id = sqlDB.insertOrThrow(TABLE_SETTINGS, null, values);
                        response = Uri.parse(SETTINGS + "/" + id);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown URI: " + uri);
                }
            }
        }
        catch (SQLiteConstraintException e){
            //Do nothing
        }
        catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return response;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
        String whereClause;
        String[] whereArgs = new String[1];
        int rowsDeleted = 0;
        if (sqlDB != null) {
            switch (uriType) {
                case ALL_UPLOADS:
                    rowsDeleted = sqlDB.delete(TABLE_UPLOAD, selection, selectionArgs);
                    break;
                case UPLOAD_ID:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        whereClause = String.format("%s=?", UploadQueueTable.COLUMN_ID);
                        whereArgs[0] = id;
                        rowsDeleted = sqlDB.delete(TABLE_UPLOAD, whereClause, whereArgs);
                    }
                    else {
                        rowsDeleted = sqlDB.delete(TABLE_UPLOAD, UploadQueueTable.COLUMN_ID + "='" + id + "' and " + selection, selectionArgs);
                    }
                    break;
                case ALL_IMAGES:
                    rowsDeleted = sqlDB.delete(TABLE_IMAGES, selection, selectionArgs);
                    break;
                case IMAGE_ID:
                    String imageId = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsDeleted = sqlDB.delete(TABLE_IMAGES, UploadQueueTable.COLUMN_ID + "='" + imageId + "'", null);
                    }
                    else {
                        rowsDeleted = sqlDB.delete(TABLE_IMAGES, UploadQueueTable.COLUMN_ID + "='" + imageId + "' and " + selection, selectionArgs);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = mUriMatcher.match(uri);
        SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
        int rowsUpdated = 0;
        if (sqlDB != null) {
            switch (uriType) {
                case ALL_UPLOADS:
                    rowsUpdated = sqlDB.update(TABLE_UPLOAD, values, selection, selectionArgs);
                    break;
                case UPLOAD_ID:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsUpdated = sqlDB.update(TABLE_UPLOAD, values, UploadQueueTable.COLUMN_ID + "= '" + id + "'", null);
                    }
                    else {
                        rowsUpdated = sqlDB.update(TABLE_UPLOAD, values, UploadQueueTable.COLUMN_ID + "= '" + id + "' and " + selection, selectionArgs);
                    }
                    break;
                case ALL_IMAGES:
                    rowsUpdated = sqlDB.update(TABLE_IMAGE, values, selection, selectionArgs);
                    break;
                case IMAGE_ID:
                    String imageId = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsUpdated = sqlDB.update(TABLE_IMAGE, values, UploadQueueTable.COLUMN_ID + "= '" + imageId + "'", null);
                    }
                    else {
                        rowsUpdated = sqlDB.update(TABLE_IMAGE, values, UploadQueueTable.COLUMN_ID + "= '" + imageId + "' and " + selection, selectionArgs);
                    }
                    break;
                case SETTINGS_ID:
                    String key = uri.getLastPathSegment();
                    String where = SettingsTable.COLUMN_KEY + " = ?";
                    String[] args = new String[]{key};
                    rowsUpdated = sqlDB.update(TABLE_SETTINGS, values, where, args);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
        return rowsUpdated;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        throw new FileNotFoundException("Unsupported uri: " + uri.toString());
    }
}
