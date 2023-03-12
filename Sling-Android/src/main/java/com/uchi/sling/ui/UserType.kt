/*
 * Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.uchi.sling.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.uchi.sling.R

class UserType : Fragment() {
    lateinit var userType: AutoCompleteTextView
    lateinit var proceedToUserDetails: FloatingActionButton

// TODO: dont enable the button until user select some value
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = resources.getStringArray(R.array.user_type).toList()
        val adapter = ArrayAdapter(requireContext(), R.layout.profile_drop_down, items)
        var userVarArg: Int? = null
        (userType as? AutoCompleteTextView)?.setAdapter(adapter)
        (userType as? AutoCompleteTextView)?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                userVarArg = position
                // Get the SharedPreferences object
                val sharedPreferences = context?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.putString("userType", adapter.getItem(position))
                editor?.apply()
            }
        proceedToUserDetails.setOnClickListener {
            findNavController().navigate(UserTypeDirections.actionUserTypeToEmailRegistration())
            val userTypeViewModel = ViewModelProvider(requireActivity())[UserTypeViewModel::class.java]
            userTypeViewModel.sharedData.value = userVarArg!!.toInt()

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

        return view
    }

}

class UserTypeViewModel : ViewModel() {
    val sharedData = MutableLiveData<Int>()
}
