package com.example.coursecatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.util.getViewModel
import com.example.coursecatalog.databinding.FragmentMainMenuBinding


class MainMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // getTerm binding for layout
        val binding: FragmentMainMenuBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_menu, container, false)

        // getTerm application context
        val application = requireNotNull(this.activity).application

        // getTerm instance of DAO for database
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // getTerm the view model for this fragment
        val vm by lazy {
            getViewModel<MainMenuViewModel>()
        }

        // bind viewmodel to layout
        binding.mainMenuViewModel = vm

        binding.lifecycleOwner = this

        vm.navigateToTermList.observe(this, Observer {term ->
            term?.let {
                this.findNavController().navigate(
                    MainMenuFragmentDirections
                        .actionMainMenuFragmentToTermListFragment())
            }

        })

        /**
         * makes sure that when the user hits the back button
         * it navigates them out of the app
         */
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressed = {
            requireActivity().finishAffinity()
        })
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


        return binding.root
    }


}