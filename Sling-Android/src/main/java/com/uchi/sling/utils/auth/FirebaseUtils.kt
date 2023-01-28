package com.uchi.sling.utils.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

@Suppress("unused")
object FirebaseUtils {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

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