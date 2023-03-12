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

package com.uchi.sling.room

data class OrganisationData(
    val orgName: String,
    val orgEmail: String,
    val orgType: String,
    val orgAddress: String,
    val orgPinCode: String,
    val orgCountry: String,
    val uid: String
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", "", "", "")
}

data class IndividualData(
    // TODO: mentor code remove string and addd list
    val mentorCode: String,
    val indCourse: String,
    val indName: String,
    val indEmail: String,
    val indStandard: String,
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", "")
}

data class MemberData(
    // TODO: Add multiple subjects remove string and add list
    val orgCode: String,
    val mentorName: String,
    val mentorEmail: String,
    val mentorDesignation: String,
    val mentorPrimarySubject: String,
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", "")
}
