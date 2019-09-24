package com.example.bradmoeller.myapplication.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.bradmoeller.myapplication.data.SharedPrefHelper;
import com.example.bradmoeller.myapplication.data.tables.SettingsTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by bradmoeller on 9/27/17.
 */

public class MySyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String AUTHORITY = "com.example.bradmoeller.myapplication.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/settings");

    ContentResolver mContentResolver;

    public MySyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    public MySyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(getClass().getSimpleName(), "MySyncAdapter onPerformSync");

        final boolean testValue = false;
        //try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            //SharedPrefHelper.putTime(getContext(), dateFormat.format(date));
            writeToFile(getContext(), dateFormat.format(date));
            //updateBooleanSetting("TestPreference", testValue);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getContext(), buildSharedPrefText(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private String buildSharedPrefText() {
        StringBuilder sb = new StringBuilder();
        Set<String> times = SharedPrefHelper.getTimes(getContext());
        for (String s : times) {
            sb.append(s).append("\n");
        }
        return sb.toString().trim();
    }

    private void writeToFile(Context context, String newDate) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, "timelog.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(newDate + "\n");

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }

    private boolean getBooleanSetting(String preference, boolean defaultValue) throws RemoteException {
        boolean result = defaultValue;
        String key = preference;
        String selection = SettingsTable.COLUMN_KEY + " = ?";
        String[] selectionArgs = new String[]{key};
        String[] projection = new String[]{SettingsTable.COLUMN_VALUE_BOOLEAN};
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(CONTENT_URI, projection, selection, selectionArgs, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    result = cursor.getInt(cursor.getColumnIndex(SettingsTable.COLUMN_VALUE_BOOLEAN)) > 0;
                }
            }
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    private void updateBooleanSetting(String preference, boolean updatedValue) throws RemoteException {
        String key = preference;
        Uri uri = CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(SettingsTable.COLUMN_VALUE_BOOLEAN, updatedValue);
        mContentResolver.insert(uri, values);
    }
}
