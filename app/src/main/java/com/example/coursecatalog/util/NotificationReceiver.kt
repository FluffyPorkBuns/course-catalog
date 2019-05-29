package com.example.coursecatalog.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.coursecatalog.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {


        val channelId = intent.extras!!.getString("channelId")!!

        createNotificationChannel(context, channelId)

        Log.i("receiverxx","$context.toString()")
        Toast.makeText(context, "received notification!", Toast.LENGTH_LONG).show()

        // builds the alert
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_school_black_24dp)
            .setContentTitle(intent.extras!!.getString("title"))
            .setContentText(intent.extras!!.getString("text"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // displays the alert
        with(NotificationManagerCompat.from(context)) {
            notify(intent.extras!!.getString("title")!!.hashCode(), builder.build())
        }

    }

        // creates notification channel for Android 8.0 or higher only
    private fun createNotificationChannel(context: Context, CHANNEL_ID: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "alertChannel"
            val descriptionText = "a channel for alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}