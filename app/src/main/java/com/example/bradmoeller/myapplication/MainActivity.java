package com.example.bradmoeller.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bradmoeller.myapplication.ImageList.ImageListActivity;
import com.example.bradmoeller.myapplication.data.SharedPrefHelper;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // An account type, in the form of a domain name
    public static final String AUTHORITY = "com.example.bradmoeller.myapplication.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.bradmoeller";
    // The account name
    public static final String ACCOUNT = "testaccount";
    // Instance fields
    Account mAccount;

    TextView textView;
    Button button;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(this);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ImageListActivity.class);
                startActivity(i);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Share File
            }
        });

        textView.setText(buildSharedPrefText());
    }

//    @Override
//    public void onClick(View view) {
//        Intent imageIntent = new Intent(Intent.ACTION_SEND);
//        Uri imageUri = Uri.parse("https://www.organicfacts.net/wp-content/uploads/2013/05/Raspberry11.jpg");
//        imageIntent.setType("image/*");
//        imageIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        startActivity(imageIntent);
//    }

    private String buildSharedPrefText() {
        StringBuilder sb = new StringBuilder();
        Set<String> times = SharedPrefHelper.getTimes(this);
        for (String s : times) {
            sb.append(s).append("\n");
        }
        return sb.toString().trim();
    }

    @Override
    public void onClick(View view) {
//        Intent intent = new Intent(this, TestService.class);
//        startService(intent);

        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        mAccount = CreateSyncAccount(this);
        Log.d(getClass().getSimpleName(), "Main Activity onClick");
        Log.d(getClass().getSimpleName(), "Using account: " + mAccount.toString());

        ContentResolver resolver = getContentResolver();
        resolver.setIsSyncable(mAccount, AUTHORITY, 1);
        resolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        resolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                600);

//        Bundle bundle = new Bundle();
//
//        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//
//        ContentResolver.requestSync(mAccount, AUTHORITY, bundle);
    }

    public Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return getAccount(context);
    }

    public Account getAccount(Context context) {
        Account[] allAccounts = AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE);
        if (allAccounts.length > 0) {
            return allAccounts[0];
        }
        return null;
    }
}
