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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uchi.sling.R
import com.uchi.sling.room.MentorData
import com.uchi.sling.utils.auth.FB_MENTOR
import com.uchi.sling.utils.auth.FB_MUID
import com.uchi.sling.utils.auth.MENTOR
import com.uchi.sling.viewmodels.MentorViewModel
import timber.log.Timber

class MentorDashboard : Fragment() {
    private lateinit var userSubTitle: TextView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileType: TextView
    private val dataBundle = Bundle()

    private var mentorName: String = ""
    private var mentorType: String = ""
    private var mentorEmail: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mentor_dashboard, container, false)
        val sharedPreferences = context?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userSubTitle = view.findViewById(R.id.sub_dashboard_title)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.user_registered_email)
        profileType = view.findViewById(R.id.user_details)
        val user = sharedPreferences?.getString("userType", "")
        setSubDashboardTitle(user)
        firebaseData()

        return view
    }

    private fun firebaseData() {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid.toString() // uid is a String that contains the UID of the current user
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection(FB_MENTOR)
        collectionRef.whereEqualTo(FB_MUID, uid).get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val mentor = doc.toObject(MentorData::class.java)
                    dataBundle.putString("mentorName", mentor.mentorName)
                    dataBundle.putString("mentorPrimarySubject", mentor.mentorDesignation)
                    dataBundle.putString("mentorEmail", mentor.mentorEmail)
                    updateUI(dataBundle)
                }
            }
            .addOnFailureListener { _ ->
                // Handle errors here
                Timber.e("MentorFragment(): Error retrieving data")
            }
    }

    private fun setSubDashboardTitle(user: String?) {
        when (user) {
            MENTOR -> {
                this.userSubTitle.text = getString(R.string.title_classes)
            }
        }
    }

    private fun updateUI(dataBundle: Bundle) {
        // Retrieve orgName and orgType from dataBundle
        val orgName = dataBundle.getString("mentorName", "")
        val orgType = dataBundle.getString("mentorPrimarySubject", "")
        val orgEmail = dataBundle.getString("mentorEmail", "")

        // Update UI using ViewModel
        val viewModel = ViewModelProvider(this)[MentorViewModel::class.java]
        viewModel.updateOrgName(orgName)
        viewModel.updateOrgType(orgType)
        viewModel.updateOrgEmail(orgEmail)

        viewModel.mName.observe(viewLifecycleOwner) {
            Timber.d(it.toString())
            profileName.text = it.toString()
        }
        viewModel.mEmail.observe(viewLifecycleOwner) {
            profileEmail.text = it.toString()
        }
        viewModel.mType.observe(viewLifecycleOwner) {
            profileType.text = it.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save orgName and orgType in the Bundle
        outState.putString("orgName", mentorName)
        outState.putString("orgType", mentorType)
        outState.putString("orgEmail", mentorEmail)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Retrieve orgName and orgType from the Bundle
        mentorName = savedInstanceState?.getString("orgName", "") ?: ""
        mentorType = savedInstanceState?.getString("orgType", "") ?: ""
        mentorEmail = savedInstanceState?.getString("orgEmail", "") ?: ""
    }
}