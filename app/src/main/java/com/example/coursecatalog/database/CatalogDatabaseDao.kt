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
    fun insert(term: TermEntity): Long

    // inserts a course into the course table
    @Insert
    fun insert(course: CourseEntity): Long

    // insert function for term course join table
    @Insert
    fun insert(termCourseJoin: TermCourseJoin)

    // updates a term in the term table
    @Update
    fun update(term: TermEntity)

    // updates a course in the course table
    @Update
    fun update(course: CourseEntity)

    // get list of terms associated with a course
    @Query("""
                SELECT * FROM term_table
                INNER JOIN term_course_join
                ON term_table.termId = term_course_join.termId
                WHERE term_course_join.courseId=:courseId
    """)
    fun getTermsForCourse(courseId: Long): LiveData<List<TermEntity>>

    // get list of courses associated with a term
    @Query("""
                SELECT * FROM course_table
                INNER JOIN term_course_join
                ON course_table.courseId = term_course_join.courseId
                WHERE term_course_join.termId=:termId
    """)
    fun getCoursesForTerm(termId: Long): LiveData<List<CourseEntity>>

    // get a list of courses not already added to this term
    @Query("""
                SELECT * FROM course_table
                WHERE course_table.courseId NOT IN (
                    SELECT course_table.courseId
                    FROM course_table
                    INNER JOIN term_course_join
                    ON course_table.courseId = term_course_join.courseId
                    WHERE term_course_join.termId = :termId)
    """)
    fun getCoursesNotInTerm(termId: Long): LiveData<List<CourseEntity>>

    // retrieves a term from the database based on the id provided
    @Query("SELECT * FROM term_table WHERE termId = :key")
    fun getTerm(key: Long): TermEntity?

    @Query("SELECT * FROM course_table WHERE courseId = :key")
    fun getCourse(key: Long): CourseEntity?

    // retrieves all terms from the database as a livedata list
    @Query("SELECT * FROM term_table ORDER BY termId DESC")
    fun getAllTerms(): LiveData<List<TermEntity>>

    // retrieves all courses from the database as a livedata list
    @Query("SELECT * FROM course_table ORDER BY courseId DESC")
    fun getAllCourses(): LiveData<List<CourseEntity>>

    // get newest course created
    @Query("""
                SELECT * FROM course_table ORDER BY courseId DESC LIMIT 1
    """)
    fun getNewestCourse(): CourseEntity?

    // gets newest term created from database
    @Query("SELECT * FROM term_table ORDER BY termId DESC LIMIT 1")
    fun getNewestTerm(): TermEntity?

    // retrieves term livedata from the database based on id
    @Query("SELECT * FROM term_table WHERE termId = :key")
    fun getTermWithId(key: Long): LiveData<TermEntity>

    // get term livedata from database based on id
    @Query("SELECT * FROM course_table WHERE courseId = :key")
    fun getCourseWithId(key: Long): LiveData<CourseEntity>

    // delete all courses from database
    @Query("DELETE FROM course_table")
    fun clearCourses()

    // deletes all terms from the database
    @Query("DELETE FROM term_table")
    fun clearTerms()
}