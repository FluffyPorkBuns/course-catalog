package com.example.coursecatalog.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.coursecatalog.database.CourseEntity
import com.example.coursecatalog.database.TermEntity

@BindingAdapter("termTitleFormatted")
fun TextView.setTermTitleFormatted(item: TermEntity?) {
    item?.let {
        text = item.termTitle
    }
}

@BindingAdapter("startDateFormatted")
fun TextView.setStartDateFormatted(item: TermEntity?) {
    item?.let {
        text = formatDateAsString(item.startDate)
    }
}

@BindingAdapter("endDateFormatted")
fun TextView.setEndDateFormatted(item: TermEntity?) {
    item?.let {
        text = formatDateAsString(item.endDate)
    }
}

@BindingAdapter("courseTitleFormatted")
fun TextView.setCourseTitleFormatted(item: CourseEntity) {
    item?.let {
        text = item.courseTitle
    }
}

@BindingAdapter("courseStartDateFormatted")
fun TextView.setCourseStartDateFormatted(item: CourseEntity) {
    item?.let {
        text = formatDateAsString(item.startDate)
    }
}

@BindingAdapter("courseEndDateFormatted")
fun TextView.setCourseEndDateFormatted(item: CourseEntity) {
    item?.let {
        text = formatDateAsString(item.endDate)
    }
}
