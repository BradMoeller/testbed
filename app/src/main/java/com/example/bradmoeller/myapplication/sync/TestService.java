package com.example.bradmoeller.myapplication.sync;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.bradmoeller.myapplication.MainActivity;

/**
 * Created by bradmoeller on 9/6/17.
 */

public class TestService extends Service {

    // Storage for an instance of the sync adapter
    private static MySyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new MySyncAdapter(getApplicationContext(), true);
            }
        }
        Log.d(TestService.class.getSimpleName(), "Test Service onCreate");
        //Toast.makeText(getApplicationContext(), "Test Service onCreate", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        Log.d(TestService.class.getSimpleName(), "Test Service onBind");
        //Toast.makeText(getApplicationContext(), "Test Service onBind", Toast.LENGTH_SHORT).show();
        return sSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("title")
                        .setContentText("content")
                        .setContentIntent(pendingIntent)
                        .setTicker("This is the ticker text")
                        .build();

        startForeground(291, notification);

        return START_STICKY;
    }
}
