package com.example.coursecatalog.assessments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.R
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentAssessmentDetailBinding
import com.example.coursecatalog.util.NotificationScheduler
import com.example.coursecatalog.util.getViewModel
import kotlinx.android.synthetic.main.fragment_assessment_detail.*
import com.example.coursecatalog.util.MessageComposer
import com.example.coursecatalog.util.formatDateAsString
import com.example.coursecatalog.validation.isNotBlank
import com.example.coursecatalog.validation.isValidDate
import com.example.coursecatalog.validation.validate


class AssessmentDetailFragment : Fragment() {

    val PICK_CONTACT_REQUEST = 1  // The request code

    var requestType = "" // tells onactivityresult to process contact as email or sms

    var requestSubject = ""

    var requestBody = ""

    // starts an activity for the user to pick a contact to send an SMS or email
    fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            if(requestType == "email") {
                type = ContactsContract.CommonDataKinds.Email.CONTENT_TYPE
            } else {
                type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(intent, PICK_CONTACT_REQUEST)
        }
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            val contactUri: Uri = data!!.data
            val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS)

            var contactInfo: String

            data!!.data?.also {
                context!!.contentResolver.query(contactUri, projection, null, null, null)?.apply {
                    moveToFirst()

                    // if request is email, get the email address from the contact
                    // otherwise get the phone number
                    if (requestType == "email") {
                        val column: Int = getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                        contactInfo = getString(column)
                        MessageComposer.composeEmail(context!!, contactInfo, requestSubject, requestBody)
                    } else {
                        val column: Int = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        contactInfo = getString(column)
                        MessageComposer.composeSMS(context!!, contactInfo, requestBody)
                    }
                }
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // getAssessment binding to fragment
        val binding: FragmentAssessmentDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_assessment_detail, container, false)

        // getAssessment reference to application
        val application = requireNotNull(this.activity).application

        // getAssessment arguments passed to this fragment
        val arguments = AssessmentDetailFragmentArgs.fromBundle(arguments!!)

        // getAssessment reference to database dao
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // gets the viewmodel object for this fragment and pass assessmentkey and datasource
        val assessmentDetailViewModel by lazy {
            getViewModel { AssessmentDetailViewModel(arguments.assessmentKey, dataSource) }
        }

//        // handle user clicking the save button
//        binding.assessmentSaveButton.setOnClickListener{
//            assessmentDetailViewModel.onSaveAssessment(
//                assessment_title.text.toString()
//            )
//            Toast.makeText(context, "Assessment saved!", Toast.LENGTH_SHORT).show()
//            assessmentDetailViewModel.onNavigateToTermDetail()
//            assessmentDetailViewModel.onTermDetailNavigated()
//        }

        /**
         * makes sure that when the user hits the back button
         * it saves the assessment and navigates them back to the term detail view
         */
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveAssessment(assessmentDetailViewModel)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // tell assessment detail viewmodel to get the courseId associated with this assessment
        assessmentDetailViewModel.getCourseId()

        // listener that navigates to term detail view when viewmodel requests it
        assessmentDetailViewModel.navigateToAssessmentList.observe(viewLifecycleOwner, Observer{
            it?.let{
                this.findNavController().navigate(
                    AssessmentDetailFragmentDirections.actionAssessmentDetailFragmentToAssessmentListFragment(it)
                )
            }
        })

        // add validation listener to title edittext field
        binding.assessmentTitle.validate({text -> text.isNotBlank()},
            "Title is required!")

        // add validation listener to dueDate field
        binding.dueDate.validate({date -> date.isValidDate()},
            "Date is required and the format should be MM/dd/yy")

        binding.saveButton.setOnClickListener{
            saveAssessment(assessmentDetailViewModel)
        }


        binding.deleteButton.setOnClickListener{
            assessmentDetailViewModel.onDelete()
            Toast.makeText(context, "assessment deleted", Toast.LENGTH_SHORT).show()
            assessmentDetailViewModel.onNavigateToAssessmentList()
            assessmentDetailViewModel.onAssessmentListNavigated()
        }

        // add notification
        binding.alarmSetButton.setOnClickListener{
            NotificationScheduler.newAssessmentNotification(context!!, arguments.assessmentKey)
        }

        // clicklistener for email button
        binding.emailButton.setOnClickListener{
            requestType = "email"
            requestSubject = "assessment ${binding.assessmentTitle.text} due ${binding.dueDate.text}"
            requestBody = "My ${binding.assessmentTypeSpinner.selectedItem} assessment '${binding.assessmentTitle.text}'" +
                    " is due on ${binding.dueDate.text}." +
            if (binding.notes.text.isNotEmpty()) {
                " Here are some notes about it: '${binding.notes.text}'"
            } else {
                " I have no notes with this assessment."
            }
            selectContact()
        }

        // clicklistener for the sms button
        binding.smsButton.setOnClickListener{
            requestType = "sms"
            requestBody = "My ${binding.assessmentTypeSpinner.selectedItem} assessment '${binding.assessmentTitle.text}'" +
                    " is due on ${binding.dueDate.text}." +
                    if (binding.notes.text.isNotEmpty()) {
                        " Here are some notes about it: '${binding.notes.text}'"
                    } else {
                        " I have no notes with this assessment."
                    }
            selectContact()
        }

        // ArrayAdapter for assessment type spinner
        ArrayAdapter.createFromResource(
            context,
            R.array.assessment_type_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.assessmentTypeSpinner.adapter = adapter
        }

        // bind viewmodel to fragment
        binding.assessmentDetailViewModel = assessmentDetailViewModel

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

    // calls on viewmodel to save assessment from ui to database and navigate to term detail fragment
    private fun saveAssessment(assessmentDetailViewModel: AssessmentDetailViewModel) {

        if(assessment_title.text.isNotEmpty() && due_date.text.toString().isValidDate()) {
            assessmentDetailViewModel.onSaveAssessment(
                assessment_title.text.toString(),
                assessment_type_spinner.selectedItem.toString(),
                due_date.text.toString(),
                notes.text.toString()
            )
            Toast.makeText(context, "assessment saved", Toast.LENGTH_SHORT).show()
            assessmentDetailViewModel.onNavigateToAssessmentList()
            assessmentDetailViewModel.onAssessmentListNavigated()
        } else {
            Toast.makeText(context, "can't save assessment because of input errors", Toast.LENGTH_SHORT).show()
        }


    }

}

