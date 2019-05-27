package com.example.coursecatalog.courses


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.R
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentCoursePickerBinding
import com.example.coursecatalog.util.getViewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class CoursePickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // getCourse binding to fragment
        val binding: FragmentCoursePickerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_course_picker, container, false)

        // getCourse reference to application
        val application = requireNotNull(this.activity).application

        // getCourse arguments passed to this fragment
        val arguments = CoursePickerFragmentArgs.fromBundle(arguments!!)

        // getCourse reference to database dao
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // gets the viewmodel object for this fragment and pass coursekey and datasource
        val coursePickerViewModel by lazy {
            getViewModel { CoursePickerViewModel(arguments.termKey, dataSource)}
        }

        // instantiate adapter for course list
        val adapter = CourseAdapter(CourseAdapter.CourseListener { courseId ->
            coursePickerViewModel.onCourseClicked(courseId)
        })

        // observe course table for changes so recyclerview updates
        coursePickerViewModel.courses.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.submitList(it)
            }
        })

        // observe for when viewmodel wants to navigate to the term detail fragment
        coursePickerViewModel.navigateToTermDetail.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    CoursePickerFragmentDirections.actionCoursePickerFragmentToTermDetailFragment(it))
                coursePickerViewModel.onTermDetailNavigated()
            }
        })

        /**
         * makes sure that when the user hits the back button
         * it navigates them back to the term detail view
         */
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            coursePickerViewModel.onNavigateToTermDetail()
            coursePickerViewModel.onTermDetailNavigated()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // bind viewmodel to fragment
        binding.coursePickerViewModel = coursePickerViewModel

        // bind course list adapter to recyclerview
        binding.coursePickerList.adapter = adapter

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }



}
