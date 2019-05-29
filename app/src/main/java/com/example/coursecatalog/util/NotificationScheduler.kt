package com.example.coursecatalog.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.example.coursecatalog.database.Assessment
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.database.CourseEntity
import java.util.*

object NotificationScheduler {

//    fun scheduleExistingNotifications(context: Context) {
//
//        var alarmCode = 0
//
//        val dataSource = CatalogDatabase.getInstance(context).catalogDatabaseDao
//
//        val courses = dataSource.getAllCoursesAsList()
//
//        val assessments = dataSource.getAllAssessmentsAsList()
//
//        // get the date object for midnight this morning
//        val yesterday = getDateBeginningOfDay(getYesterday(Date()))
//
//        // create course notification alarms for courses starting or ending today or later
//        for(course in courses) {
//
//            // generate a unique alarmId by adding the word "course" and the courseId and getting the hashcode
//            val startAlarmId = "startcourse${course.courseId}".hashCode()
//
//            // generate a unique alarmId by adding the word "course" and the courseId and getting the hashcode
//            val endAlarmId = "endcourse${course.courseId}".hashCode()
//
//            // schedule alert for course if the day before it is today or in the future
//            if (course.startDate.after(yesterday)) {
//
//                addAlarm(context, "course",
//                    course.courseTitle,
//                    "starts ${formatDateAsString(course.startDate)}",
//                    "is starting soon!",
//                    "course start",
//                    startAlarmId,
//                    getYesterday(getDateBeginningOfDay(course.startDate)).time)
//            }
//
//            // schedule alert for courses that end today or out in the future
//            if (course.endDate.after(yesterday)) {
//                addAlarm(context, "course",
//                    course.courseTitle,
//                    "ends ${formatDateAsString(course.endDate)}",
//                    "is ending soon!",
//                    "course end",
//                    endAlarmId,
//                    getYesterday(getDateBeginningOfDay(course.endDate)).time)
//            }
//        }
//
//        // create notifications alarms for all assessments due today or later
//        for(assessment in assessments) {
//            // schedule alert for assessments that start today
//            if(assessment.dueDate.after(yesterday)) {
//                val alertId = "assessment${assessment.assessmentId}".hashCode()
//
//                addAlarm(context, "${assessment.type} assessment",
//                    assessment.title,
//                    "due ${formatDateAsString(assessment.dueDate)}!",
//                    "is due soon!",
//                    "assessment due",
//                    alertId,
//                    getYesterday(getDateBeginningOfDay(assessment.dueDate)).time)
//            }
//        }
//    }

    // add alarm to phone
    private fun addAlarm(
        context: Context,
        itemType: String,
        itemName: String,
        callToActionShort: String,
        callToAction: String,
        channelId: String,
        alarmCode: Int,
        alarmTime: Long
    ) {
        // get alarm manager object from OS
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context.applicationContext, NotificationReceiver::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("title", "$itemType $callToActionShort")
            putExtra("text", "Your $itemType: '$itemName' $callToAction!")
            putExtra("channelId", channelId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, alarmCode, intent, 0)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    fun newAssessmentNotification(context: Context, assessmentId: Long) {

        val dataSource = CatalogDatabase.getInstance(context).catalogDatabaseDao

        val assessment = dataSource.getAssessment(assessmentId)!!

        val alertId = "assessment${assessment.assessmentId}".hashCode()

        addAlarm(context, "${assessment.type} assessment",
            assessment.title,
            "due ${formatDateAsString(assessment.dueDate)}!",
            "is due soon!",
            "assessment due",
            alertId,
            getYesterday(getDateBeginningOfDay(assessment.dueDate)).time)


        Toast.makeText(context, "assessment reminder added", Toast.LENGTH_SHORT).show()
    }

    fun newCourseNotification(context: Context, courseId: Long) {

        val dataSource = CatalogDatabase.getInstance(context).catalogDatabaseDao

        val course = dataSource.getCourse(courseId)!!

        // generate a unique alarmId by adding the word "course" and the courseId and getting the hashcode
        val startAlarmId = "startcourse${course.courseId}".hashCode()

        // generate a unique alarmId by adding the word "course" and the courseId and getting the hashcode
        val endAlarmId = "endcourse${course.courseId}".hashCode()

        addAlarm(context, "course",
            course.courseTitle,
            "starts ${formatDateAsString(course.startDate)}",
            "is starting soon!",
            "course start",
            startAlarmId,
            getYesterday(getDateBeginningOfDay(course.startDate)).time)

        addAlarm(context, "course",
            course.courseTitle,
            "ends ${formatDateAsString(course.endDate)}",
            "is ending soon!",
            "course end",
            endAlarmId,
            getYesterday(getDateBeginningOfDay(course.endDate)).time)


        Toast.makeText(context, "course reminder added", Toast.LENGTH_SHORT).show()
    }
}