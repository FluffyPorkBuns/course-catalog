package com.example.coursecatalog.database

import androidx.room.TypeConverter
import java.util.*

// converts complex data types to ones SQLite can handle
class Converters {

    // converts date in Long format from database to Java Date object
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // converts Java Date object to Long for SQLite database
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}