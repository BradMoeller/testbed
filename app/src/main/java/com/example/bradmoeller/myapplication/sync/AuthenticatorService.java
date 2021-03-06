package com.example.bradmoeller.myapplication.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Created by bradmoeller on 9/27/17.
 */

public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private MyAuthenticator mAuthenticator;
    @Override

    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MyAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
