package com.example.coursecatalog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.coursecatalog.util.getDateNextMonth
import java.util.*

/**
 * defines the term entity for the room to use in the database
 * assessment has a one-to-many relationship with course
 * all assessments associated with course will be deleted (cascade)
 * if course is deleted
 */
@Entity(tableName = "assessment_table",
    foreignKeys = [ForeignKey(
        entity = CourseEntity::class,
        parentColumns = arrayOf("courseId"),
        childColumns = arrayOf("courseId"),
        onDelete = CASCADE
    )]
)
data class Assessment(

    @PrimaryKey(autoGenerate = true)
    var assessmentId: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "New Assessment",

    @ColumnInfo(name = "type")
    var type: String = "objective",

    @ColumnInfo(name= "due_date")
    var dueDate: Date = getDateNextMonth(),

    @ColumnInfo(name = "notes")
    var notes: String = "",

    @ColumnInfo
    var courseId: Long = 0L

)