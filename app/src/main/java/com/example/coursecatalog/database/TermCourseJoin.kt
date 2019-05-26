package com.example.coursecatalog.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "term_course_join",
    primaryKeys = arrayOf("termId", "courseId"),
    foreignKeys = arrayOf(
        ForeignKey(entity = TermEntity::class,
            parentColumns = arrayOf("termId"),
            childColumns = arrayOf("termId")),
        ForeignKey(entity = CourseEntity::class,
            parentColumns = arrayOf("courseId"),
            childColumns = arrayOf("courseId"))
    ))
data class TermCourseJoin(
    val termId: Long,
    val courseId: Long,
    val courseStatus: String,
    val courseNote: String
)