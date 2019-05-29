package com.example.coursecatalog.database

import androidx.room.*
import java.util.*

// mentor object will be nested under CourseEntity
// keeps a logical separation between mentor and course
data class Mentor(
    var name: String = "",
    var phone: String = "",
    var email: String = ""
)

/**
 * defines the course entity for the room to use in the database
 * assessments are represented as a 1 to many relationship
 * using foreign key constraint with RESTRICT so that a term cannot be
 * deleted without first deleting all the courses
  */

@Entity(tableName = "course_table",
    foreignKeys = [ForeignKey(
        entity = TermEntity::class,
        parentColumns = arrayOf("termId"),
        childColumns = arrayOf("termId"),
        onDelete = ForeignKey.RESTRICT
    )]
)
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

    @ColumnInfo
    var termId: Long = 0L,

    // mentor object is nested in course and has a 1 to 1 relationship
    @Embedded
    val mentor: Mentor = Mentor()

)