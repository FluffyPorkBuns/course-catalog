package com.example.coursecatalog.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// mentor object will be nested under CourseEntity
// keeps a logical separation between mentor and course
data class Mentor(
    var name: String = "",
    var phone: String = "",
    var email: String = ""
)

// defines the course entity for the room to use in the database
@Entity(tableName = "course_table")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    var courseId: Long = 0L,

    @ColumnInfo(name = "course_title")
    var courseTitle: String = "New Course",

    @ColumnInfo(name= "start_date")
    var startDate: Date = Date(),

    @ColumnInfo(name = "end_date")
    var endDate: Date = Date(),

    @ColumnInfo(name = "status")
    var status: String = "plan to take",

    @ColumnInfo(name = "notes")
    var notes: String = "",

    // mentor object is nested in course and has a 1 to 1 relationship
    @Embedded
    val mentor: Mentor = Mentor()

)