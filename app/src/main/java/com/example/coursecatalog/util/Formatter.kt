package com.example.coursecatalog.util

import java.text.SimpleDateFormat
import java.util.*

// formats a Date object and returns a string in MM/dd/yy format
fun formatDateAsString(date: Date): String {
    val simpleDateFormat = SimpleDateFormat("MM/dd/yy")

    return simpleDateFormat.format(date)
}

fun formatStringAsDate(date: String): Date {
    return SimpleDateFormat("MM/dd/yy").parse(date)
}