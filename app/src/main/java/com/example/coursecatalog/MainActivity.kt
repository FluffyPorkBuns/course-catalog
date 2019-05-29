package com.example.coursecatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coursecatalog.util.NotificationScheduler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // open the main menu fragment
        setContentView(R.layout.activity_main)

        NotificationScheduler.scheduleExistingNotifications(this)

//        // get alarm manager object from OS
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        // schedule notifications for terms
//        // sends user to main activity when they tap the alert
//        val intent = Intent(this, NotificationReceiver::class.java)
//        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//
//        alarmManager.set(
//            AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + 1000,
//            pendingIntent
//        )
    }
}
