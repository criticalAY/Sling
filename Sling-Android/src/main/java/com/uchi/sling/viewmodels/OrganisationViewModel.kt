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

package com.uchi.sling.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrganisationViewModel : ViewModel() {

    private val organisationName = MutableLiveData<String>()
    val orgName: LiveData<String> = organisationName

    private val organisationEmail = MutableLiveData<String>()
    val orgEmail: LiveData<String> = organisationEmail

    private val organisationType = MutableLiveData<String>()
    val orgType: LiveData<String> = organisationType

    fun updateOrgName(name: String) {
        organisationName.value = name
    }

    fun updateOrgEmail(email: String) {
        organisationEmail.value = email
    }

    fun updateOrgType(type: String) {
        organisationType.value = type
    }
}