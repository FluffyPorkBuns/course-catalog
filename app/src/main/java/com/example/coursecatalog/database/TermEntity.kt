package com.example.coursecatalog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.coursecatalog.util.getDateNextMonth
import java.util.*

// defines the term entity for the room to use in the database

@Entity(tableName = "term_table")
data class TermEntity(
    @PrimaryKey(autoGenerate = true)
    var termId: Long = 0L,

    @ColumnInfo(name = "term_title")
    var termTitle: String = "",

    @ColumnInfo(name= "start_date")
    var startDate: Date = Date(),

    @ColumnInfo(name = "end_date")
    var endDate: Date = getDateNextMonth()

)