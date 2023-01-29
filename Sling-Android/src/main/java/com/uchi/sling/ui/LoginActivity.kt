
package com.uchi.sling.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.GoogleAuthProvider
import com.uchi.sling.R
import com.uchi.sling.utils.auth.FirebaseUtils.existingUserSignIn
import com.uchi.sling.utils.auth.FirebaseUtils.firebaseAuth
import com.uchi.sling.utils.snackbars.showSnackbar
import timber.log.Timber

@Suppress("unused")
class LoginActivity : AppCompatActivity() {
    private var oneTapClient: SignInClient? = null
    private lateinit var userEmailInput: TextInputEditText
    private lateinit var userEmail: String
    private lateinit var userPasswordInput: TextInputEditText
    private lateinit var userPassword: String
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var logonBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        Timber.i("LoginActivity started")
        webClient = getString(R.string.web_client_id)

        emailInputLayout = findViewById(R.id.email_input_text_layout)
        passwordInputLayout = findViewById(R.id.password_input_text_layout)
        logonBtn = findViewById(R.id.login_button)
        userEmailInput = findViewById(R.id.user_email)
        userPasswordInput = findViewById(R.id.user_password)

        if (!userEmailInput.text.isNullOrEmpty()) {
            userEmail = userEmailInput.text.toString()
        }
        if (!userPasswordInput.text.isNullOrEmpty()) {
            userPassword = userPasswordInput.text.toString()
        }

        logonBtn.setOnClickListener {
            initiateLogin()
        }

    }

    private fun initiateLogin() {
        userEmail = userEmailInput.text.toString()
        userPassword = userPasswordInput.text.toString()
        if (!existingUserSignIn(userEmail, userPassword)) {
            showSnackbar(getString(R.string.email_not_found))

        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        // updateUI(FirebaseUtils.firebaseUser)

    }
//    @Suppress("unused", "UNREACHABLE_CODE", "unused_parameter")
//    private fun updateUI(currentUser: FirebaseUser?) {
//        TODO("Not yet implemented")
//
//    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val googleCredential = oneTapClient?.getSignInCredentialFromIntent(data)
        val idToken = googleCredential?.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.d("signInWithCredential:success")
                            // val user = firebaseAuth.currentUser
                            // updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.w("signInWithCredential:failure", task.exception)
                            // updateUI(null)
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Timber.d("No ID token!")
            }
        }

    }
    companion object {
        var webClient: String = "null"
    }
}