package com.example.coursecatalog.terms


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
import com.example.coursecatalog.courses.CourseAdapter
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentTermDetailBinding
import com.example.coursecatalog.util.getViewModel
import kotlinx.android.synthetic.main.fragment_term_detail.*


class TermDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // getTerm binding to fragment
        val binding: FragmentTermDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_term_detail, container, false)

        // getTerm reference to application
        val application = requireNotNull(this.activity).application

        // getTerm arguments passed to this fragment
        val arguments = TermDetailFragmentArgs.fromBundle(arguments!!)

        // getTerm reference to database dao
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // gets the viewmodel object for this fragment and pass termkey and datasource
        val termDetailViewModel by lazy {
            getViewModel { TermDetailViewModel(arguments.termKey, dataSource)}
        }

        // instantiate adapter for course list
        val adapter = CourseAdapter(CourseAdapter.CourseListener { courseId ->
            termDetailViewModel.onCourseClicked(courseId)
        })

        // observe term table for changes so recyclerview updates
        termDetailViewModel.courses.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.submitList(it)
            }
        })

        // handle user clicking the save button
        binding.termSaveButton.setOnClickListener{
            saveTerm(termDetailViewModel)
        }

        // observe for user clicking on a term to go to the detail view
        termDetailViewModel.navigateToTermList.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    TermDetailFragmentDirections.actionTermDetailFragmentToTermListFragment())
                termDetailViewModel.onTermListNavigated()
            }
        })

        termDetailViewModel.navigateToCourseDetail.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    TermDetailFragmentDirections.actionTermDetailFragmentToCourseDetailFragment(it)
                )
                termDetailViewModel.onCourseNavigated()
            }
        })

        /**
         * makes sure that when the user hits the back button
         * it saves the term and navigates them back to the term list
         */
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveTerm(termDetailViewModel)
            termDetailViewModel.onNavigateToTermList()
            termDetailViewModel.onTermListNavigated()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // bind viewmodel to fragment
        binding.termDetailViewModel = termDetailViewModel

        // bind course list adapter to recyclerview
        binding.termCourseList.adapter = adapter

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun saveTerm(termDetailViewModel: TermDetailViewModel) {
        termDetailViewModel.onSaveTerm(
            term_title.text.toString(),
            start_date_text.text.toString(),
            start_date.text.toString()
        )
        Toast.makeText(context, "Term saved!", Toast.LENGTH_SHORT).show()
        termDetailViewModel.onNavigateToTermList()
        termDetailViewModel.onTermListNavigated()
    }

}
