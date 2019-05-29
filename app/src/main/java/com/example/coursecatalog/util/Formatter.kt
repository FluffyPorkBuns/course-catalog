package com.example.coursecatalog.util

import java.text.SimpleDateFormat
import java.util.*

// formats a Date object and returns a string in MM/dd/yy format
fun formatDateAsString(date: Date): String {
    val simpleDateFormat = SimpleDateFormat("MM/dd/yy")

    return simpleDateFormat.format(date)
}

// convert a string into a date object
fun formatStringAsDate(date: String): Date {
    return SimpleDateFormat("MM/dd/yy").parse(date)
}

// take a date and set the time to midnight that morning
fun getDateBeginningOfDay(date: Date): Date {
    val calendar = GregorianCalendar()
    calendar.time = date
    calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE))
    calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND))
    calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND))

    return calendar.time
}

// take a date and return a date one day in the past
fun getYesterday(date: Date): Date {
    val calendar = GregorianCalendar()
    calendar.time = date
    calendar.add(Calendar.HOUR, -24)
    return calendar.time
}

// take a date and return a date 30 days in the future
fun getDateNextMonth(): Date {
    val calendar = GregorianCalendar()
    calendar.time = Date()
    calendar.add(Calendar.HOUR, 720)
    return calendar.time
}