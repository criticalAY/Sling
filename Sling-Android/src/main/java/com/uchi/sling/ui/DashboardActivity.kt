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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uchi.sling.R
import com.uchi.sling.utils.auth.INDIVIDUAL
import com.uchi.sling.utils.auth.MENTOR
import com.uchi.sling.utils.auth.ORGANISATION

class DashboardActivity : AppCompatActivity() {
    private val organisationDashboard = OrganisationDashboard()
    private val mentorDashboard = MentorDashboard()
    private val accountFragment = AccountFragment()
    private val statsFragment = StatsFragment()
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        bottomNavigation = findViewById(R.id.dashboard_navigation)
        navigateFragments(organisationDashboard)
        val sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val user = sharedPreferences?.getString("userType", "")

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    when (user.toString()) {
                        ORGANISATION -> {
                            navigateFragments(organisationDashboard)
                        }
                        MENTOR -> {
                            navigateFragments(mentorDashboard)
                        }

                        INDIVIDUAL -> {

                        }
                    }
                    true
                }
                R.id.item_account_settings -> {
                    navigateFragments(accountFragment)
                    true
                }
                R.id.item_statistics -> {
                    navigateFragments(statsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateFragments(beginningFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.dashboard_frame_layout, beginningFragment)
        transaction.commit()
    }
}