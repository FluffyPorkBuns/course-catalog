package com.example.coursecatalog.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
        text = formatDate(item.startDate)
    }
}

@BindingAdapter("endDateFormatted")
fun TextView.setEndDateFormatted(item: TermEntity?) {
    item?.let {
        text = formatDate(item.endDate)
    }
}
