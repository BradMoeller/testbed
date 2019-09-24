package com.example.bradmoeller.myapplication.notifications

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.bradmoeller.myapplication.R
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.gson.Gson
import kotlinx.android.synthetic.main.notifications_activity.*
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException


/**
 * Created by bradmoeller on 1/22/18.
 */
class NotificationsActivity : AppCompatActivity() {

    private val background = newFixedThreadPoolContext(2, "bg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications_activity)

        send_button.setOnClickListener {
            onSendClick()
        }
    }

    private fun onSendClick() {
        doAsync {
            val result = post()
            uiThread {
                Toast.makeText(this@NotificationsActivity, result, Toast.LENGTH_SHORT).show()
                Log.e("Error", result)
            }
        }
    }

    @Throws(IOException::class)
    private fun getAccessToken(): String? {

        val googleCredential = GoogleCredential
                .fromStream(assets.open("service_account.json"))
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        googleCredential.refreshToken()
        Log.e("Access Token: ", googleCredential.accessToken)
        return googleCredential.accessToken
    }

    //val job = launch(background)

    private fun post() : String? {

        val jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()

        var body = RequestBody.create(jsonMediaType, buildNotificaion())
        var request = Request.Builder()
                .addHeader("Authorization", "Bearer ${getAccessToken()}")
                .addHeader("Content-Type", "application/json; UTF-8")
                .url("https://fcm.googleapis.com/v1/projects/forever-android/messages:send")
                .post(body)
                .build()
        var response = client.newCall(request).execute();
        return response.body()?.string();
    }

    private fun buildNotificaion() : String{
        var payload = MNotificationPayload(MMessage(token_text.text.toString(), MNotification(message_text.text.toString(), "Title")))
        return Gson().toJson(payload)
    }
}