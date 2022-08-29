package com.example.googlebooksapi.viewmodel.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


private const val TAG = "BookFBM"
class BookFBM: FirebaseMessagingService(){

    /**
     * When a new token is created, either the user
     * clean app data or reinstall.
     *
     */

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }
    /**
     * When a new message is send in the campaign
     */


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived: $message")
        // todo create the notification


    }
}