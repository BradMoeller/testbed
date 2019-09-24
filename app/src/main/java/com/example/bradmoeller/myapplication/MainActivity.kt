package com.example.bradmoeller.myapplication

import android.accounts.Account
import android.accounts.AccountManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentResolver.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.bradmoeller.myapplication.ImageList.ImageListActivity
import com.example.bradmoeller.myapplication.clean.ui.CleanActivity
import com.example.bradmoeller.myapplication.contacts.ContactsActivity
import com.example.bradmoeller.myapplication.lifecycle.LifecycleActivity
import com.example.bradmoeller.myapplication.merge.MergeActivity
import com.example.bradmoeller.myapplication.notifications.NotificationsActivity
import com.example.bradmoeller.myapplication.video.VideoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Instance fields
    internal var mAccount: Account? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener(this)
        job_button.setOnClickListener(this)

        button2.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, ImageListActivity::class.java)
            startActivity(i)
        }

        button3.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_SEND)
            val imageUri = Uri.parse("https://www.organicfacts.net/wp-content/uploads/2013/05/Raspberry11.jpg")
            imageIntent.type = "image/*"
            imageIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(imageIntent)
        }

        contacts_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, ContactsActivity::class.java)
            startActivity(i)
        }

        notifications_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, NotificationsActivity::class.java)
            startActivity(i)
        }

        merge_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, MergeActivity::class.java)
            startActivity(i)
        }

        lifecycle_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, LifecycleActivity::class.java)
            startActivity(i)
        }

        kotlin_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, KotlinTestActivity::class.java)
            startActivity(i)
        }

        clean_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, CleanActivity::class.java)
            startActivity(i)
        }

        video_button.setOnClickListener {
            val i = Intent()
            i.setClass(this@MainActivity, VideoActivity::class.java)
            startActivity(i)
        }

    }

    override fun onClick(view: View) {
        if (view.id == R.id.button) {
            startSyncAdapter()
        } else if (view.id == R.id.job_button) {
            startJobScheduler()
        }

    }

    private fun startJobScheduler() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(JobInfo.Builder(MyJobScheduler.JOB_ID,
                ComponentName(this, MyJobScheduler::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic((1000 * 3600).toLong())
                .build())
    }

    private fun startSyncAdapter() {
        //        Intent intent = new Intent(this, TestService.class);
        //        startService(intent);

        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        mAccount = CreateSyncAccount(this)
        Log.d(javaClass.simpleName, "Main Activity onClick")
        Log.d(javaClass.simpleName, "Using account: " + mAccount!!.toString())

        val resolver = contentResolver
        setIsSyncable(mAccount, AUTHORITY, 1)
        setSyncAutomatically(mAccount, AUTHORITY, true)
        addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                3600)

        //        Bundle bundle = new Bundle();
        //
        //        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        //        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        //
        //        ContentResolver.requestSync(mAccount, AUTHORITY, bundle);
    }

    fun CreateSyncAccount(context: Context): Account? {
        // Create the account type and default account
        val newAccount = Account(
                ACCOUNT, ACCOUNT_TYPE)
        // Get an instance of the Android account manager
        val accountManager = context.getSystemService(
                Context.ACCOUNT_SERVICE) as AccountManager
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

        return getAccount(context)
    }

    fun getAccount(context: Context): Account? {
        val allAccounts = AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE)
        return if (allAccounts.size > 0) {
            allAccounts[0]
        } else null
    }

    companion object {

        // An account type, in the form of a domain name
        val AUTHORITY = "com.example.bradmoeller.myapplication.provider"
        // An account type, in the form of a domain name
        val ACCOUNT_TYPE = "com.example.bradmoeller"
        // The account name
        val ACCOUNT = "testaccount"
    }
}
