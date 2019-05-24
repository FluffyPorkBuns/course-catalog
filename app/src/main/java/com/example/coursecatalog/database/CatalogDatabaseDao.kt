package com.example.coursecatalog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CatalogDatabaseDao {

    // inserts a term into the term table
    @Insert
    fun insert(term: TermEntity)

    // updates a term in the term table
    @Update
    fun update(term: TermEntity)

    // retrieves a term from the database based on the id provided
    @Query("SELECT * FROM term_table WHERE termId = :key")
    fun get(key: Long): TermEntity?

    // retrieves all terms from the database as a livedata list
    @Query("SELECT * FROM term_table ORDER BY termId DESC")
    fun getAllTerms(): LiveData<List<TermEntity>>

    // gets newest term created from database
    @Query("SELECT * FROM term_table ORDER BY termId DESC LIMIT 1")
    fun getNewestTerm(): TermEntity?

    // retrieves term livedata from the database based on id
    @Query("SELECT * FROM term_table WHERE termId = :key")
    fun getTermWithId(key: Long): LiveData<TermEntity>

    // deletes all terms from the database
    @Query("DELETE FROM term_table")
    fun clearTerms()
}