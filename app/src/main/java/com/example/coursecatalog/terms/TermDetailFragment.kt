package com.example.coursecatalog.terms


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.R
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentTermDetailBinding
import com.example.coursecatalog.util.getViewModel
import com.example.coursecatalog.validation.isNotBlank
import com.example.coursecatalog.validation.isValidDate
import com.example.coursecatalog.validation.validate
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

        // add validation listener to title edittext field
        binding.termTitle.validate({text -> text.isNotBlank()},
            "Title is required!")

        // add validation listener to dueDate field
        binding.startDate.validate({date -> date.isValidDate()},
            "Date is required and the format should be MM/dd/yy")

        // add validation listener to dueDate field
        binding.endDate.validate({date -> date.isValidDate()},
            "Date is required and the format should be MM/dd/yy")

        // handle user clicking the save button
        binding.saveButton.setOnClickListener{
            saveTerm(termDetailViewModel)
        }

        // handle delete button
        binding.deleteButton.setOnClickListener{
            termDetailViewModel.onDelete()
        }

        binding.courseListButton.setOnClickListener{

            if(term_title.text.toString().isNotBlank() && start_date.text.toString().isValidDate()
                && end_date.text.toString().isValidDate()) {

                termDetailViewModel.onSaveTerm(
                    term_title.text.toString(),
                    start_date.text.toString(),
                    end_date.text.toString()
                )
                Toast.makeText(context, "term saved", Toast.LENGTH_SHORT).show()
                termDetailViewModel.onNavigateToCourseList()
                termDetailViewModel.onCourseNavigated()
            } else {
                Toast.makeText(context, "can't continue, some fields are invalid", Toast.LENGTH_SHORT).show()
            }
        }

        // pops up an error message if the user tries to delete a term with courses
        termDetailViewModel.cannotDelete.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, "Can't delete term while there are courses added! Delete the courses first.", Toast.LENGTH_LONG).show()
                termDetailViewModel.onCanDelete()
            }
        })

        // observe for user clicking on a term to go to the detail view
        termDetailViewModel.navigateToTermList.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    TermDetailFragmentDirections.actionTermDetailFragmentToTermListFragment())
                termDetailViewModel.onTermListNavigated()
            }
        })

        termDetailViewModel.navigateToCourseList.observe(viewLifecycleOwner, Observer{
            it?.let {
                this.findNavController().navigate(
                    TermDetailFragmentDirections.actionTermDetailFragmentToCourseListFragment(it)
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
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // bind viewmodel to fragment
        binding.termDetailViewModel = termDetailViewModel

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun saveTerm(termDetailViewModel: TermDetailViewModel) {

        if(term_title.text.toString().isNotBlank() && start_date.text.toString().isValidDate()
            && end_date.text.toString().isValidDate()) {

            termDetailViewModel.onSaveTerm(
                term_title.text.toString(),
                start_date.text.toString(),
                end_date.text.toString()
            )
            Toast.makeText(context, "term saved", Toast.LENGTH_SHORT).show()
            termDetailViewModel.onNavigateToTermList()
            termDetailViewModel.onTermListNavigated()
        } else {
            Toast.makeText(context, "can't continue, some fields are invalid", Toast.LENGTH_SHORT).show()
        }


    }

}
