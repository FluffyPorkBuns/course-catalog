package com.example.coursecatalog.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CatalogDatabaseDao {

    // inserts a term into the term table
    @Insert
    fun insert(term: TermEntity): Long

    // inserts a course into the course table
    @Insert
    fun insert(course: CourseEntity): Long

    @Insert
    fun insert(assessment: Assessment): Long

    // updates a term in the term table
    @Update
    fun update(term: TermEntity)

    // updates a course in the course table
    @Update
    fun update(course: CourseEntity)

    // update assessment
    @Update
    fun update(assessment: Assessment)

    // get list of assessments associated with course
    @Query("""
                SELECT * FROM assessment_table
                WHERE courseId=:courseId
                ORDER BY assessmentId
                DESC
    """)
    fun getAssessmentsByCourse(courseId: Long): LiveData<List<Assessment>>

    // get a course associated with an assessment by the assessmentId
    @Query("""
                SELECT course_table.courseId FROM course_table
                INNER JOIN assessment_table
                ON course_table.courseId = assessment_table.courseId
                where assessment_table.assessmentId = :assessmentKey
                LIMIT 1
    """)
    fun getCourseIdByAssessmentId(assessmentKey: Long): Long

    // get term associated with a course
    @Query("""
                SELECT * FROM term_table
                INNER JOIN course_table
                ON term_table.termId = course_table.termId
                WHERE course_table.courseId=:courseId LIMIT 1
    """)
    fun getTermIdForCourse(courseId: Long): Long

    // get list of courses associated with a term
    @Query("""
                SELECT * FROM course_table
                INNER JOIN term_table
                ON course_table.courseId = course_table.courseId
                WHERE course_table.termId=:termId
    """)
    fun getCoursesForTerm(termId: Long): LiveData<List<CourseEntity>>

    // retrieves a term from the database based on the id provided
    @Query("SELECT * FROM term_table WHERE termId = :key")
    fun getTerm(key: Long): TermEntity?

    // get course object from database using the id
    @Query("SELECT * FROM course_table WHERE courseId = :key")
    fun getCourse(key: Long): CourseEntity?

    // get assessment object from database using the id
    @Query("SELECT * FROM assessment_table WHERE assessmentId = :key")
    fun getAssessment(key: Long): Assessment?

    // get all assessments as a list
    @Query("SELECT * FROM assessment_table")
    fun getAllAssessmentsAsList(): List<Assessment>

    // get assessment livedata object from database using id
    @Query("SELECT * FROM assessment_table WHERE assessmentId = :key")
    fun getAssessmentById(key: Long): LiveData<Assessment>

    // retrieves all terms from the database as a livedata list
    @Query("SELECT * FROM term_table ORDER BY termId DESC")
    fun getAllTerms(): LiveData<List<TermEntity>>

    // retrieves all courses from the database as a livedata list
    @Query("SELECT * FROM course_table ORDER BY courseId DESC")
    fun getAllCourses(): LiveData<List<CourseEntity>>

    // retrieves all courses from the database as a livedata list
    @Query("SELECT * FROM course_table ORDER BY courseId DESC")
    fun getAllCoursesAsList(): List<CourseEntity>

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

    // get all terms as a list
    @Query("SELECT * FROM term_table")
    fun getAllTermsAslist(): List<TermEntity>

    // delete assessment by id
    @Query("DELETE FROM assessment_table WHERE assessmentId = :key")
    fun deleteAssessmentById(key: Long)

    // delete course by id
    @Query("DELETE FROM course_table WHERE courseId = :key")
    fun deleteCourseById(key: Long)

    // deletes term from the database
    @Query("DELETE FROM term_table WHERE termId = :key")
    fun deleteTermById(key: Long)
}