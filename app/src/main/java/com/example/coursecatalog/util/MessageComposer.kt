package com.example.coursecatalog.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import javax.security.auth.Subject

object MessageComposer {

    // creates and starts an intent to open the user's email client and send an email
    fun composeEmail(context: Context, address: String, subject: String, text: String) {

        val uriText = "mailto:$address?subject=${Uri.encode(subject)}&body=${Uri.encode(text)}"

        val uriResult = Uri.parse(uriText)

        val intent = Intent(Intent.ACTION_SENDTO).apply {
//            type = "*/*"
//            putExtra(Intent.EXTRA_EMAIL, addresses)
//            putExtra(Intent.EXTRA_SUBJECT, subject)
//            putExtra(Intent.EXTRA_TEXT, text)
        }
        intent.data = uriResult
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            Toast.makeText(context, "Email sent to $address", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No default mail client found!", Toast.LENGTH_SHORT).show()
        }
    }

    // creates and starts an intent to open the user's email client and send an email
    fun composeSMS(context: Context, phone: String, text: String) {

        val uriText = "smsto:$phone"

        val uriResult = Uri.parse(uriText)

        val intent = Intent(Intent.ACTION_SENDTO, uriResult)
//        intent.data = uriResult
        intent.putExtra("sms_body", text)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            Toast.makeText(context, "SMS sent to $phone", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to open SMS program!", Toast.LENGTH_SHORT).show()
        }
    }

}