package com.example.bradmoeller.myapplication.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.bradmoeller.myapplication.R
import kotlinx.android.synthetic.main.contacts_activity.*


/**
 * Created by bradmoeller on 1/17/18.
 */
class ContactsActivity : AppCompatActivity() {

    data class ContactItem(val name: String, val phone: String)
    private val READ_CONTACTS_REQUEST: Int = 1936

    private var contactItemsList: MutableList<ContactItem> = mutableListOf()
    private var contactListAdapter: ContactListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Contacts"

        setContentView(R.layout.contacts_activity)
        contacts_list.layoutManager = LinearLayoutManager(applicationContext)


        when(checkCalenderPermissions()) {
            true -> setAdapter()
            false -> requestPermissions()
        }
    }

    fun getContacts(): List<ContactItem> {
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)

        val result: MutableList<ContactItem> = mutableListOf()


        while (contacts.moveToNext()) {
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            result.add(ContactItem(name, phoneNumber))
        }


        contacts.close()

        return result
    }

    fun checkCalenderPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS), READ_CONTACTS_REQUEST)
    }

    fun setAdapter() {
        contactListAdapter = ContactListAdapter(getContacts(), onContactClicked)
        contacts_list.adapter = contactListAdapter
    }

    private val onContactClicked: (x: ContactItem) -> Unit = {
        println(42)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            READ_CONTACTS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                        grantResults.size > 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    setAdapter()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.

            else -> {
                // Ignore all other requests.
            }
        }
    }
}