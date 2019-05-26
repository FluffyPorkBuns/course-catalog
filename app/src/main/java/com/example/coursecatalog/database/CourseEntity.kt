package com.example.coursecatalog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// defines the course entity for the room to use in the database
@Entity(tableName = "course_table")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    var courseId: Long = 0L,

    @ColumnInfo(name = "course_title")
    var courseTitle: String = "",

    @ColumnInfo(name= "start_date")
    var startDate: Date = Date(),

    @ColumnInfo(name = "end_date")
    var endDate: Date = Date()

)