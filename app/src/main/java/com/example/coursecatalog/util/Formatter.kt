package com.example.coursecatalog.util

import java.text.SimpleDateFormat
import java.util.*

// formats a Date object and returns a string in MM/dd/yy format
fun formatDate(date: Date): String {
    val simpleDateFormat = SimpleDateFormat("MM/dd/yy")

    return simpleDateFormat.format(date)
}