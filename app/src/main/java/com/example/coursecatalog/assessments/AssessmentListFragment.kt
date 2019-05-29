package com.example.coursecatalog.assessments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.R
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentAssessmentListBinding
import com.example.coursecatalog.util.getViewModel


class AssessmentListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // getTerm binding to fragment
        val binding: FragmentAssessmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_assessment_list, container, false)

        // getTerm reference to application
        val application = requireNotNull(this.activity).application

        // getTerm arguments passed to this fragment
        val arguments = AssessmentListFragmentArgs.fromBundle(arguments!!)

        // getTerm reference to database dao
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // gets the viewmodel object for this fragment and pass termkey and datasource
        val assessmentListViewModel by lazy {
            getViewModel { AssessmentListViewModel(arguments.courseKey, dataSource)}
        }

        // instantiate adapter for course list
        val adapter = AssessmentAdapter(AssessmentAdapter.AssessmentListener { assessmentId ->
            assessmentListViewModel.onNavigateToAssessmentDetail(assessmentId)
        })

        // observe term table for changes so recyclerview updates
        assessmentListViewModel.courses.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.submitList(it)
            }
        })

        // observe for user clicking on a term to go to the detail view
        assessmentListViewModel.navigateToAssessmentDetail.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    AssessmentListFragmentDirections.actionAssessmentListFragmentToAssessmentDetailFragment(it))
                assessmentListViewModel.onAssessmentDetailNavigated()
            }
        })

        assessmentListViewModel.navigateToCourseDetail.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    AssessmentListFragmentDirections.actionAssessmentListFragmentToCourseDetailFragment(it)
                )
                assessmentListViewModel.onCourseDetailNavigated()
            }
        })

        /**
         * makes sure that when the user hits the back button
         * it saves the term and navigates them back to the term list
         */
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            assessmentListViewModel.onNavigateToCourseDetail()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // bind viewmodel to fragment
        binding.assessmentListViewModel = assessmentListViewModel

        // bind course list adapter to recyclerview
        binding.assessmentList.adapter = adapter

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

}
