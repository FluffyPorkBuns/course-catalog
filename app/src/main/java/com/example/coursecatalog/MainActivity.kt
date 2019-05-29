package com.example.coursecatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.coursecatalog.util.NotificationScheduler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // open the main menu fragment
        setContentView(R.layout.activity_main)
    }
}
