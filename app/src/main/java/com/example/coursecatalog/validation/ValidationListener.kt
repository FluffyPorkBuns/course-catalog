package com.example.coursecatalog.validation

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.databinding.adapters.TextViewBindingAdapter
import java.text.ParseException
import java.text.SimpleDateFormat

fun EditText.validateAfterChange(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

// listens for changes to edittext field and marks it as having an error if it doesn't validate
fun EditText.validate(validator: (String) -> Boolean, message: String) {
    this.validateAfterChange {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.text.toString())) null else message
}

// return true if email is in a valid format or if empty (optional field)
fun String.isValidEmail(): Boolean {
    return this.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// return true if text is in a valid phone number format or if empty (optional field)
fun String.isValidPhone(): Boolean {
    return this.isEmpty() || return Patterns.PHONE.matcher(this).matches()
}

// return true if text is not blank
fun String.isNotBlank(): Boolean {
    return this.isNotEmpty()
}

// return true if date is in a valid format
fun String.isValidDate(): Boolean {
    val simpleDateFormat = SimpleDateFormat("MM/dd/yy")

    simpleDateFormat.isLenient = false

    try {
        simpleDateFormat.parse(this)
    } catch (e: ParseException) {
        return false
    }

    // if try worked then we can assume the date is valid
    return true
}