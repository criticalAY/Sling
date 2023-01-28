package com.uchi.sling.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.GoogleAuthProvider
import com.uchi.sling.R
import com.uchi.sling.utils.auth.FirebaseUtils.existingUserSignIn
import com.uchi.sling.utils.auth.FirebaseUtils.firebaseAuth
import com.uchi.sling.utils.googleData.GoogleSignInData
import com.uchi.sling.utils.snackbars.showSnackbar
import timber.log.Timber

@Suppress("unused")
class LoginActivity : AppCompatActivity() {
    private lateinit var userEmailInput: TextInputEditText
    private lateinit var userEmail: String
    private lateinit var userPasswordInput: TextInputEditText
    private lateinit var userPassword: String
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var newUser: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var logonBtn: Button
    private lateinit var googleSignIn: TextView
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val requestCode: Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        Timber.i("LoginActivity started")

        emailInputLayout = findViewById(R.id.email_input_text_layout)
        passwordInputLayout = findViewById(R.id.password_input_text_layout)
        logonBtn = findViewById(R.id.login_button)
        userEmailInput = findViewById(R.id.user_email)
        userPasswordInput = findViewById(R.id.user_password)
        newUser = findViewById(R.id.action_new_user)
        forgotPassword = findViewById(R.id.action_forgot_password)
        googleSignIn = findViewById(R.id.action_google_sign_in)

        if (!userEmailInput.text.isNullOrEmpty()) {
            userEmail = userEmailInput.text.toString()
        }
        if (!userPasswordInput.text.isNullOrEmpty()) {
            userPassword = userPasswordInput.text.toString()
        }

        newUser.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        forgotPassword.setOnClickListener {
            // TODO: make an activity
        }

        logonBtn.setOnClickListener {
            // TODO put a check to check null and correct email password format
            initiateLogin()
        }
        FirebaseApp.initializeApp(this)
        googleSignIn.setOnClickListener {
            Timber.d("Google Sign in clicked")
            signInGoogle()
        }

    }

    @Suppress("DEPRECATION")
    private fun signInGoogle() {
        Timber.w("Google sign in started")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, requestCode)
    }

    private fun initiateLogin() {
        userEmail = userEmailInput.text.toString()
        userPassword = userPasswordInput.text.toString()
        if (!existingUserSignIn(userEmail, userPassword)) {
            showSnackbar(getString(R.string.email_not_found))

        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            Timber.w("Google Sign in successful")
            handleResult(task)
        }
    }
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                GoogleSignInData.setEmail(this, account.email.toString())
                GoogleSignInData.setUsername(this, account.displayName.toString())
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

}