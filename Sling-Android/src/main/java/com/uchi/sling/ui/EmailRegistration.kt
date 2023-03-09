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

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.uchi.sling.R
import com.uchi.sling.utils.Utility
import com.uchi.sling.utils.auth.FirebaseUtils

class EmailRegistration : Fragment() {
    lateinit var nextButton: Button
    lateinit var registrationEmail: TextInputEditText
    lateinit var registrationEmailLayout: TextInputLayout
    lateinit var passwordText: TextInputEditText
    lateinit var rePasswordText: TextInputEditText
    lateinit var passwordTextLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextButton = view.findViewById(R.id.user_input_next_button)
        var userProfile: Int = -1
        val userTypeViewModel = ViewModelProvider(requireActivity())[UserTypeViewModel::class.java]
        userTypeViewModel.sharedData.observe(viewLifecycleOwner) { data ->
            userProfile = data
        }

        registrationEmail = view.findViewById(R.id.registration_email)
        passwordText = view.findViewById(R.id.password_t1)
        rePasswordText = view.findViewById(R.id.password_t2)
        passwordTextLayout = view.findViewById(R.id.registration_password_layout)
        registrationEmailLayout = view.findViewById(R.id.email_layout)
        passwordCheck()

        nextButton.setOnClickListener {
            if (registrationEmailCheck()) {
                FirebaseUtils.newUserEmailSignUp(registrationEmail.text.toString(), rePasswordText.text.toString())
                findNavController().navigate(EmailRegistrationDirections.actionEmailRegistrationToUserDetails(userProfile))
            } else Toast.makeText(context, "fdfd", Toast.LENGTH_SHORT).show()

        }
    }

    private fun registrationEmailCheck(): Boolean {
        if (!Utility.isEmailValid(registrationEmail.text)) {
            registrationEmailLayout.isErrorEnabled = true
            registrationEmailLayout.error = resources.getString(R.string.invalid_email)
            Utility.emailCheck(registrationEmail, registrationEmailLayout, resources.getString(R.string.invalid_email))
            return false
        } else {
            registrationEmailLayout.isErrorEnabled = false
        }
        return true
    }

    private fun passwordCheck() {
        passwordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //  not needed
            }

            override fun afterTextChanged(p0: Editable?) {
                Utility.isValidPassword(passwordText.text.toString())
            }

        })

        rePasswordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //  not needed
            }

            override fun afterTextChanged(p0: Editable?) {
                Utility.isValidPassword(p0.toString())
                if (passwordText.text.toString() != rePasswordText.text.toString()) {
                    passwordTextLayout.isErrorEnabled = true
                    passwordTextLayout.error = getString(R.string.incorrect_password)
                } else {
                    passwordTextLayout.isErrorEnabled = false
                }
            }
        })

    }

}