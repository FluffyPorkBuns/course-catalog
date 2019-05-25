package com.example.coursecatalog.terms


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.coursecatalog.R
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.databinding.FragmentTermDetailBinding
import com.example.coursecatalog.util.getViewModel
import kotlinx.android.synthetic.main.fragment_term_detail.*


class TermDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get binding to fragment
        val binding: FragmentTermDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_term_detail, container, false)

        // get reference to application
        val application = requireNotNull(this.activity).application

        // get arguments passed to this fragment
        val arguments = TermDetailFragmentArgs.fromBundle(arguments!!)

        // get reference to database dao
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // gets the viewmodel object for this fragment and pass termkey and datasource
        val termDetailViewModel by lazy {
            getViewModel { TermDetailViewModel(arguments.termKey, dataSource)}
        }

        // handle user clicking the save button
        binding.termSaveButton.setOnClickListener{
            termDetailViewModel.onSaveTerm(term_title.text.toString())
        }

        // bind viewmodel to fragment
        binding.termDetailViewModel = termDetailViewModel

        // bind lifecycleowner
        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

}
