package com.uchi.sling.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.uchi.sling.R

/**
 * A simple [Fragment] subclass.
 * Use the [UserType.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserType : Fragment() {
    lateinit var userType: AutoCompleteTextView
    lateinit var proceedToUserDetails: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proceedToUserDetails.setOnClickListener {
            view.findNavController().navigate(R.id.action_userType_to_userDetails)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_type, container, false)
        userType = view.findViewById(R.id.user_profile_type)
        proceedToUserDetails = view.findViewById(R.id.action_proceed)

        val items = listOf("Material", "Design", "Components", "Android")
        val adapter = ArrayAdapter(requireContext(), R.layout.profile_drop_down, items)
        (userType as? AutoCompleteTextView)?.setAdapter(adapter)

        return view
    }

}