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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uchi.sling.R
import com.uchi.sling.room.IndividualData
import com.uchi.sling.room.MentorData
import com.uchi.sling.room.OrganisationData
import com.uchi.sling.room.ParentData
import com.uchi.sling.utils.auth.CODE
import com.uchi.sling.utils.auth.FB_CODES
import com.uchi.sling.utils.auth.FirebaseUtils
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [UserDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserDetails : Fragment() {
    private val userArgs: UserDetailsArgs by navArgs()
    private var uProfileCode: Int? = null

    lateinit var nextButton: Button

    // organisation member
    lateinit var orgCode: TextInputEditText
    lateinit var memberName: TextInputEditText
    lateinit var memberEmail: TextInputEditText
    lateinit var memberDesignation: TextInputEditText
    lateinit var memberPrimaryField: TextInputEditText

    lateinit var organisationJoiningCode: String

    // individual
    lateinit var mentorCode: TextInputEditText
    lateinit var individualCourse: TextInputEditText
    lateinit var individualName: TextInputEditText
    lateinit var individualEmail: TextInputEditText
    lateinit var individualStd: TextInputEditText

    // organisation
    lateinit var orgName: TextInputEditText
    lateinit var orgEmail: TextInputEditText
    lateinit var orgType: AutoCompleteTextView
    lateinit var orgTypeText: String
    lateinit var orgAddress: TextInputEditText
    lateinit var orgCountryPinCode: TextInputEditText
    lateinit var orgCountry: TextInputEditText

    lateinit var orgEmailLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId: Int = when (userArgs.userTypeArg) {
            0 -> R.layout.organisation_layout
            1 -> R.layout.organisation_member_layout
            else -> R.layout.fragment_user_details
        }
        return inflater.inflate(layoutId, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uProfileCode = userArgs.userTypeArg
        nextButton = view.findViewById(R.id.user_input_next_button)
        when (uProfileCode) {
            0 -> {
                orgEmailLayout = view.findViewById(R.id.email_layout)
                orgName = view.findViewById(R.id.org_name)
                orgType = view.findViewById(R.id.org_type)
                orgAddress = view.findViewById(R.id.org_address)
                orgCountryPinCode = view.findViewById(R.id.org_country_pin_code)
                orgCountry = view.findViewById(R.id.org_country)
                orgEmail = view.findViewById(R.id.email)

                val items = resources.getStringArray(R.array.organisation_type).toList()
                val adapter = ArrayAdapter(requireContext(), R.layout.type_org_dropdown, items)
                (orgType as? AutoCompleteTextView)?.setAdapter(adapter)
                (orgType as? AutoCompleteTextView)?.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        orgTypeText = adapter.getItem(position).toString()
                    }

                orgEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
                //     val items = resources.getStringArray(R.array.organisation_type).toList()
                //      val adapter = ArrayAdapter(requireContext(), R.layout.profile_drop_down, items)
                orgType = view.findViewById<AppCompatAutoCompleteTextView>(R.id.org_type)
                orgType.setAdapter(adapter)
                organisationRegistration()
            }
            1 -> {
                orgCode = view.findViewById(R.id.organisation_code)
                memberName = view.findViewById(R.id.organisation_member_name)
                memberEmail = view.findViewById(R.id.organisation_member_email)
                memberDesignation = view.findViewById(R.id.organisation_member_designation)
                memberPrimaryField = view.findViewById(R.id.organisation_member_primary_subject)

                memberEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
                orgMemberRegistration()

            }
            2 -> {
                mentorCode = view.findViewById(R.id.metor_code)
                individualEmail = view.findViewById(R.id.individual_email)
                individualName = view.findViewById(R.id.individual_name)
                individualCourse = view.findViewById(R.id.individual_courses)
                individualStd = view.findViewById(R.id.individual_std)
                individualEmail.setText(FirebaseUtils.firebaseUser?.email)
                individualRegistration()
            }
        }
//        nextButton.setOnClickListener {
//            if (!Utility.isEmailValid(orgEmail.text)) {
//                orgEmailLayout.isErrorEnabled = true
//                orgEmailLayout.error = resources.getString(R.string.invalid_email)
//                Utility.emailCheck(orgEmail, orgEmailLayout, resources.getString(R.string.invalid_email))
//            } else orgEmailLayout.isErrorEnabled = false
//        }

        //  val orgData = OrganisationData(orgNameText, orgEmailText, orgTypeText, orgAddressText, orgCountryPinCodeText, orgCountryText)

    }

    private fun individualRegistration() {
        val individualData = IndividualData(
            mentorCode.text.toString(),
            individualCourse.text.toString(),
            individualName.text.toString(),
            individualEmail.text.toString(),
            individualStd.text.toString()
        )
        nextButton.setOnClickListener {
            FirebaseUtils.uploadFbData(individualData)
            Timber.i("Uploaded organisation Individual data to Firebase")
            goToDashboard()
        }
    }
    private fun orgMemberRegistration() {
        organisationJoiningCode = "null"
        val currentUser = FirebaseAuth.getInstance().currentUser

        orgCode.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                organisationJoiningCode = fetchParent(orgCode.text.toString()).toString()

            }
        }

        val userId = currentUser?.uid
        nextButton.setOnClickListener {
            if (organisationJoiningCode.isEmpty() || organisationJoiningCode == "null") {
                val codeError = view?.findViewById<TextInputLayout>(R.id.joining_code_layout)
                codeError?.isErrorEnabled = true
                codeError?.error = getString(R.string.invalid_joining_code)
            } else {
                val codeError = view?.findViewById<TextInputLayout>(R.id.joining_code_layout)
                codeError?.isErrorEnabled = false
                val memberData = MentorData(
                    orgCode.text.toString(),
                    memberName.text.toString(),
                    memberEmail.text.toString(),
                    memberDesignation.text.toString(),
                    memberPrimaryField.text.toString(),
                    organisationJoiningCode,
                    userId.toString()
                )
                FirebaseUtils.uploadFbData(memberData)
                Timber.i("Uploaded organisation member data to Firebase")
                goToDashboard()
            }
        }

    }

    private fun organisationRegistration() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        nextButton.setOnClickListener {
            Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show()
            val orgData = OrganisationData(
                orgName.text.toString(),
                orgEmail.text.toString(),
                "hardco ded",
                orgAddress.text.toString(),
                orgCountryPinCode.text.toString(),
                orgCountry.text.toString(),
                userId.toString()
            )
            Timber.i("Uploaded organisation data to Firebase")
            FirebaseUtils.uploadFbData(orgData)
            goToDashboard()
        }
    }

    fun fetchParent(joiningCode: String): Task<String> {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection(FB_CODES)
        val query = collectionRef.whereEqualTo(CODE, joiningCode)

        return query.get().continueWith { task ->
            if (task.isSuccessful) {
                val document = task.result.documents.firstOrNull()
                val parentData = document?.toObject(ParentData::class.java)
                parentData?.uid ?: throw RuntimeException("Parent document not found")
            } else {
                throw task.exception ?: RuntimeException("Unknown error")
            }
        }
    }

    private fun goToDashboard() {
        val intent = Intent(activity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }
    // TODO: diff codes/methods according to profile type
    // TODO: Init sql database here and store user data and the type of profile they made
    // TODO: Save this data to firebase
    // TODO: Take user to appropriate dashboard
}

//    private fun fund() {
//        orgEmail.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if(!Utility.isEmailValid(orgEmail.text.toString())){
//                    orgEmailLayout.isErrorEnabled = true
//                    orgEmailLayout.error = getString(R.string.invalid_email)
//                }
//                else orgEmailLayout.isErrorEnabled = false
//
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // not needed
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // not needed
//            }
//        })
//    }
