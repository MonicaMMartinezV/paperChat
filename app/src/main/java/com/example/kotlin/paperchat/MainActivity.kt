package com.example.kotlin.paperchat

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    // Create the Variables for the inputs.
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var signOutButton: TextView
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
// Assign the inputs to the variables.
        emailInput    = findViewById(R.id.editText_username)
        passwordInput = findViewById(R.id.editText_password)

// Get the login button from xml view.
        loginButton = findViewById(R.id.button_login)
// Set an listener for the Click Event.
        loginButton.setOnClickListener {
// Print the email and password.
            val emailInputTxt     = emailInput.text.toString()
            val passwordInputTxt  = passwordInput.text.toString()

            if (isValidEmail(emailInputTxt) && isPassNotEmpty(passwordInputTxt)) {
                println("Email: $emailInputTxt")
                println("Password: $passwordInputTxt")
                Log.i("Test Credentials",
                    "Email: $emailInputTxt and Password $passwordInputTxt")
                logIn(emailInputTxt, passwordInputTxt)
            }
        }

        signupButton = findViewById(R.id.button_signup)

        signupButton.setOnClickListener {
            val emailInputTxt    = emailInput.text.toString()
            val passwordInputTxt = passwordInput.text.toString()

            if (isValidEmail(emailInputTxt) && isPassNotEmpty(passwordInputTxt)) {
                println("Email: $emailInputTxt")
                println("Password: $passwordInputTxt")
                Log.i("Test Credentials",
                    "Email: $emailInputTxt and Password $passwordInputTxt")
                signUp(emailInputTxt, passwordInputTxt)
            }
        }

        signOutButton = findViewById(R.id.signOutTextV)

        signOutButton.setOnClickListener {
            signOut()
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        if (email == "") {
            return false
        }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (email.matches(emailPattern.toRegex())) {
            return true
        }

        showError("Incorrect email format.")
        return false
    }

    // Function to validate if password is empty.
    private fun isPassNotEmpty(password: String): Boolean {
        return password != ""
    }

    // Function to show error message in a notification banner
    private fun showError(message: String) {
        val toast = Toast.makeText(this, message,
            Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private fun reload() {
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showError("Authentication Successfully.")
                } else {
                    showError("User not registered.")
                }
            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showError("New user created.")
                    reload()
                } else {
                    showError("User already registered.")
                }
            }
    }

    private fun signOut() {
        auth.signOut()
        showError("Logged Out.")
    }
}