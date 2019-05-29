package com.example.coursecatalog.courses

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
import com.example.coursecatalog.assessments.AssessmentAdapter
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentCourseDetailBinding
import com.example.coursecatalog.util.NotificationScheduler
import com.example.coursecatalog.util.getViewModel
import com.example.coursecatalog.validation.*
import kotlinx.android.synthetic.main.fragment_course_detail.*


class CourseDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // getCourse binding to fragment
        val binding: FragmentCourseDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_detail, container, false)

        // getCourse reference to application
        val application = requireNotNull(this.activity).application

        // getCourse arguments passed to this fragment
        val arguments = CourseDetailFragmentArgs.fromBundle(arguments!!)

        // getCourse reference to database dao
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // gets the viewmodel object for this fragment and pass coursekey and datasource
        val courseDetailViewModel by lazy {
            getViewModel { CourseDetailViewModel(arguments.courseKey, dataSource) }
        }

        /**
         * makes sure that when the user hits the back button
         * it saves the course and navigates them back to the term detail view
         */
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveCourse(courseDetailViewModel)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // add validation listener to title edittext field
        binding.courseTitle.validate({text -> text.isNotBlank()},
            "Title is required!")

        // add validation listener to dueDate field
        binding.startDate.validate({date -> date.isValidDate()},
            "Date is required and the format should be MM/dd/yy")

        // add validation listener to dueDate field
        binding.endDate.validate({date -> date.isValidDate()},
            "Date is required and the format should be MM/dd/yy")

        binding.mentorEmail.validate({email -> email.isValidEmail()},
            "Email must be in a valid 'mentor@example.com' format or left blank")

        binding.mentorPhone.validate({phone -> phone.isValidPhone()},
            "Phone must be in a valid format or left blank")

        binding.addAlarmButton.setOnClickListener{
            // save alarm for course
            NotificationScheduler.newCourseNotification(context!!, arguments.courseKey)
        }

        // tell course detail viewmodel to get the termId associated with this course
        courseDetailViewModel.getTermId()

        // handle user clicking the save button
        binding.saveButton.setOnClickListener{
            saveCourse(courseDetailViewModel)
        }

        // handler for delete button
        binding.deleteButton.setOnClickListener{
            courseDetailViewModel.onDelete()
        }

        binding.assessmentButton.setOnClickListener{
            if(course_title.text.isNotBlank() && start_date.text.toString().isValidDate()
                && end_date.text.toString().isValidDate() && mentor_phone.text.toString().isValidPhone()
                && mentor_email.text.toString().isValidEmail()) {

                // call on viewmodel to save course to database
                courseDetailViewModel.onSaveCourse(
                    course_title.text.toString(),
                    course_status_spinner.selectedItem.toString(),
                    start_date.text.toString(),
                    end_date.text.toString(),
                    mentor_name.text.toString(),
                    mentor_phone.text.toString(),
                    mentor_email.text.toString(),
                    notes.text.toString()
                )
                // display toast message to user
                Toast.makeText(context, "course saved", Toast.LENGTH_SHORT).show()

                // navigate to term detail view
                courseDetailViewModel.onNavigateToAssessmentList()
                courseDetailViewModel.onAssessmentListNavigated()
            } else {
                // display error message if form doesn't validate
                Toast.makeText(context, "can't continue because of input errors", Toast.LENGTH_SHORT).show()
            }
        }

        // listener that navigates to term detail view when viewmodel requests it
        courseDetailViewModel.navigateToCourseList.observe(viewLifecycleOwner, Observer{
            it?.let{
                this.findNavController().navigate(
                    CourseDetailFragmentDirections.actionCourseDetailFragmentToCourseListFragment(it)
                )
            }
        })

        // listener that navigates to assessment detail view when viewmodel requests it
        courseDetailViewModel.navigateToAssessmentList.observe(viewLifecycleOwner, Observer{
            it?.let{

                this.findNavController().navigate(
                    CourseDetailFragmentDirections.actionCourseDetailFragmentToAssessmentListFragment(it)
                )
            }
        })

        // ArrayAdapter for course status spinner
        ArrayAdapter.createFromResource(
            context,
            R.array.course_status_array,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.courseStatusSpinner.adapter = adapter
        }

        // bind viewmodel to fragment
        binding.courseDetailViewModel = courseDetailViewModel

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

    // calls on viewmodel to save course from ui to database and navigate to term detail fragment
    private fun saveCourse(courseDetailViewModel: CourseDetailViewModel) {

        if(course_title.text.isNotBlank() && start_date.text.toString().isValidDate()
            && end_date.text.toString().isValidDate() && mentor_phone.text.toString().isValidPhone()
            && mentor_email.text.toString().isValidEmail()) {

            // call on viewmodel to save course to database
            courseDetailViewModel.onSaveCourse(
                course_title.text.toString(),
                course_status_spinner.selectedItem.toString(),
                start_date.text.toString(),
                end_date.text.toString(),
                mentor_name.text.toString(),
                mentor_phone.text.toString(),
                mentor_email.text.toString(),
                notes.text.toString()
            )
            // display toast message to user
            Toast.makeText(context, "course saved", Toast.LENGTH_SHORT).show()

            // navigate to term detail view
            courseDetailViewModel.onNavigateToCourseList()
            courseDetailViewModel.onCourseListNavigated()
        } else {
            // display error message if form doesn't validate
            Toast.makeText(context, "can't save because of input errors", Toast.LENGTH_SHORT).show()
        }


    }

}

