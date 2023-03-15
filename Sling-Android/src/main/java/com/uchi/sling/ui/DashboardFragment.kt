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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uchi.sling.R
import com.uchi.sling.room.OrganisationData
import com.uchi.sling.utils.JoiningCodeGenerator
import com.uchi.sling.utils.auth.*
import com.uchi.sling.viewmodels.OrganisationViewModel
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class DashboardFragment : Fragment() {

    lateinit var profileName: TextView
    lateinit var profileEmail: TextView
    lateinit var profileType: TextView
    private var orgName: String = ""
    private var orgType: String = ""
    private var orgEmail: String = ""
    // Create a new bundle to store the retrieved data
    private val dataBundle = Bundle()

    private lateinit var userSubTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.user_registered_email)
        profileType = view.findViewById(R.id.user_details)
        val codegen = view.findViewById<Button>(R.id.code_generate)
        codegen.setOnClickListener {
            runBlocking {
                val c = JoiningCodeGenerator().generateCode()
                Toast.makeText(context, c.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val sharedPreferences = context?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences?.getString("userType", "")
        userSubTitle = view.findViewById(R.id.sub_dashboard_title)
        setSubDashboardTitle(user.toString())

        firebaseData()
        return view
    }

    private fun setSubDashboardTitle(user: String?) {
        when (user) {
            ORGANISATION -> {
                this.userSubTitle.text = getString(R.string.title_mentors)
            }
            MENTOR -> {
                this.userSubTitle.text = getString(R.string.title_classes)
            }
            INDIVIDUAL -> {
                this.userSubTitle.text = getString(R.string.title_rooms)
            }
        }
    }

    fun firebaseData() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString() // uid is a String that contains the UID of the current user
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection(FB_ORGANISATION)
        collectionRef.whereEqualTo(FB_UID, uid).get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val org = doc.toObject(OrganisationData::class.java)
                    dataBundle.putString("orgName", org.orgName)
                    dataBundle.putString("orgType", org.orgType)
                    dataBundle.putString("orgEmail", org.orgEmail)
                    updateUI(dataBundle)
                }
            }
            .addOnFailureListener { _ ->
                // Handle errors here
                Timber.e("DashboardFragment(): Error retrieving data")
            }
    }

    fun updateUI(dataBundle: Bundle) {
        // Retrieve orgName and orgType from dataBundle
        val orgName = dataBundle.getString("orgName", "")
        val orgType = dataBundle.getString("orgType", "")
        val orgEmail = dataBundle.getString("orgEmail", "")

        // Update UI using ViewModel
        val viewModel = ViewModelProvider(this)[OrganisationViewModel::class.java]
        viewModel.updateOrgName(orgName)
        viewModel.updateOrgType(orgType)
        viewModel.updateOrgEmail(orgEmail)

        viewModel.orgName.observe(viewLifecycleOwner) {
            Timber.d(it.toString())
            profileName.text = it.toString()
        }
        viewModel.orgEmail.observe(viewLifecycleOwner) {
            profileEmail.text = it.toString()
        }
        viewModel.orgType.observe(viewLifecycleOwner) {
            profileType.text = it.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save orgName and orgType in the Bundle
        outState.putString("orgName", orgName)
        outState.putString("orgType", orgType)
        outState.putString("orgEmail", orgEmail)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Retrieve orgName and orgType from the Bundle
        orgName = savedInstanceState?.getString("orgName", "") ?: ""
        orgType = savedInstanceState?.getString("orgType", "") ?: ""
        orgEmail = savedInstanceState?.getString("orgEmail", "") ?: ""
    }

}