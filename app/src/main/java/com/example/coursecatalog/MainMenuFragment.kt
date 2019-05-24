package com.example.coursecatalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.coursecatalog.database.CatalogDatabase
import com.example.coursecatalog.database.getViewModel
import com.example.coursecatalog.databinding.FragmentMainMenuBinding
import kotlinx.android.synthetic.main.fragment_main_menu.*
import javax.security.auth.login.LoginException


class MainMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // get binding for layout
        val binding: FragmentMainMenuBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_menu, container, false)

        // get application context
        val application = requireNotNull(this.activity).application

        // get instance of DAO for database
        val dataSource = CatalogDatabase.getInstance(application).catalogDatabaseDao

        // get the view model for this fragment
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


        return binding.root
    }


}