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

package com.uchi.sling.utils.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.uchi.sling.room.IndividualData
import com.uchi.sling.room.MentorData
import com.uchi.sling.room.OrganisationData
import timber.log.Timber

const val FB_MENTOR_COLLECTION = "mentor"
const val FB_INDIVIDUAL_COLLECTION = "individual"
const val FB_ORGANISATION_COLLECTION = "organisation"

@Suppress("unused")
object FirebaseUtils {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    val firebaseDatabase = FirebaseDatabase.getInstance().reference
    val userId = firebaseUser?.uid

    private val orgRef: DatabaseReference by lazy { firebaseDatabase.child(FB_ORGANISATION_COLLECTION) }
    private val mentorRef = firebaseDatabase.child(FB_MENTOR_COLLECTION)
    private val individualRef = firebaseDatabase.child(FB_INDIVIDUAL_COLLECTION)

    /** upload data as a organisation **/
    fun uploadFbData(data: OrganisationData) {
        val orgUser = orgRef.child(userId.toString())
        val key = orgUser.push().key
        if (key != null) {
            // should never be null
            orgUser.child(key).setValue(data)
        }
    }
    /** upload data as a mentor **/
    fun uploadFbData(data: MentorData) {
        val mentorUser = mentorRef.child(userId.toString())
        val key = mentorUser.push().key
        if (key != null) {
            // should never be null
            mentorUser.child(key).setValue(data)
        }
    }

    /** upload data as a individual **/
    fun uploadFbData(data: IndividualData) {
        val individualUser = individualRef.child(userId.toString())
        val key = individualUser.push().key
        if (key != null) {
            // should never be null
            individualUser.child(key).setValue(data)
        }
    }

//    private fun emailSignUp() {
//        if (identicalPassword()) {
//            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
//            userEmail = etEmail.text.toString().trim()
//            userPassword = etPassword.text.toString().trim()
//
//            /*create a user*/
//            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        sendEmailVerification()
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
//                    } else {
//                        toast("failed to Authenticate !")
//                    }
//                }
//        }
//    }
    /** reset password by passing the user email **/
    fun userPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Password reset Email sent")
                }
            }
    }

    /** existing user can sign in using their email and password **/
    fun existingUserSignIn(userEmail: String, userPassword: String): Boolean {
        var userSuccess = false
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                userSuccess = if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithEmail:success")
                    true
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("signInWithEmail:failure", task.exception)
                    false
                }
            }
        return userSuccess
    }

    /** allows new user to sign up using their email and password **/
    fun newUserEmailSignUp(userEmail: String, userPassword: String): Boolean {
        // TODO add a check to see if the password matches in two password fields and is not empty
        var userSuccess = false
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                userSuccess = if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    true
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("createUserWithEmail:failure", task.exception)
                    false
                }
            }
        return userSuccess
    }

    /** send verification email to the new user. This will only
     *  work if the firebase user is not null.
     */
    private fun sendEmailVerification(): Boolean {
        var success = false
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    success = true
                }
            }

        }
        return success
    }

    /** sign out the user **/
    fun signOut() {
        firebaseAuth.signOut()
    }
}