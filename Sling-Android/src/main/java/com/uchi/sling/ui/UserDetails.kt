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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.uchi.sling.R
import com.uchi.sling.utils.snackbars.showSnackbar

/**
 * A simple [Fragment] subclass.
 * Use the [UserDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserDetails : Fragment() {
    private val userArgs: UserDetailsArgs by navArgs()
    var uProfileCode: Int? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uProfileCode = userArgs.userTypeArg
        this.showSnackbar(uProfileCode.toString(), Snackbar.LENGTH_LONG)
        // TODO: diff codes/methods according to profile type
        // TODO: Init sql database here and store user data and the type of profile they made
        // TODO: Save this data to firebase
        // TODO: Take user to appropriate dashboard
    }

}