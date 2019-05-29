package com.example.coursecatalog.assessments

import android.os.Bundle
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


class AssessmentDetailFragment : Fragment() {

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
        assessmentDetailViewModel.navigateToCourseDetail.observe(viewLifecycleOwner, Observer{
            it?.let{
                this.findNavController().navigate(
                    AssessmentDetailFragmentDirections.actionAssessmentDetailFragmentToCourseDetailFragment(it)
                )
            }
        })

        binding.deleteButton.setOnClickListener{
            assessmentDetailViewModel.onDelete()
            Toast.makeText(context, "assessment deleted", Toast.LENGTH_SHORT).show()
            assessmentDetailViewModel.onNavigateToCourseDetail()
            assessmentDetailViewModel.onCourseDetailNavigated()
        }

        // add notification
        binding.alarmSetButton.setOnClickListener{
            NotificationScheduler.newAssessmentNotification(context!!, arguments.assessmentKey)
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
        assessmentDetailViewModel.onSaveAssessment(
            assessment_title.text.toString(),
            assessment_type_spinner.selectedItem.toString(),
            due_date.text.toString(),
            notes.text.toString()
        )
        Toast.makeText(context, "assessment saved", Toast.LENGTH_SHORT).show()
        assessmentDetailViewModel.onNavigateToCourseDetail()
        assessmentDetailViewModel.onCourseDetailNavigated()
    }

}

