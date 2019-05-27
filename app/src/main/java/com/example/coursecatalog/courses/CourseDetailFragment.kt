package com.example.coursecatalog.courses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.R
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentCourseDetailBinding
import com.example.coursecatalog.util.getViewModel
import kotlinx.android.synthetic.main.fragment_course_detail.*
import kotlinx.android.synthetic.main.list_item_course.*


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

//        // handle user clicking the save button
//        binding.courseSaveButton.setOnClickListener{
//            courseDetailViewModel.onSaveCourse(
//                course_title.text.toString()
//            )
//            Toast.makeText(context, "Course saved!", Toast.LENGTH_SHORT).show()
//            courseDetailViewModel.onNavigateToTermDetail()
//            courseDetailViewModel.onTermDetailNavigated()
//        }


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

        // tell course detail viewmodel to get the termId associated with this course
        courseDetailViewModel.getTermId()

        // handle user clicking the save button
        binding.courseSaveButton.setOnClickListener{
            saveCourse(courseDetailViewModel)
        }

        // listener that navigates to term detail view when viewmodel requests it
        courseDetailViewModel.navigateToTermDetail.observe(viewLifecycleOwner, Observer{
            it?.let{
                Log.i("term id bug", "$it")
                this.findNavController().navigate(
                    CourseDetailFragmentDirections.actionCourseDetailFragmentToTermDetailFragment(it)
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
        courseDetailViewModel.onSaveCourse(
            course_title.text.toString(),
            course_status_spinner.toString(),
            start_date.text.toString(),
            end_date.text.toString(),
            mentor_name.text.toString(),
            mentor_phone.text.toString(),
            mentor_email.text.toString(),
            notes.text.toString()
        )
        Toast.makeText(context, "Course saved!", Toast.LENGTH_SHORT).show()
        courseDetailViewModel.onNavigateToTermDetail()
        courseDetailViewModel.onTermDetailNavigated()
    }

}

