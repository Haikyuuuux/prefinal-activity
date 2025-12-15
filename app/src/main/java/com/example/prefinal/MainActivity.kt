package com.example.prefinal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var textViewEmail: TextView
    private lateinit var textViewGreeting: TextView
    private lateinit var buttonLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Initialize views
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewGreeting = findViewById(R.id.textViewGreeting)
        buttonLogout = findViewById(R.id.buttonLogout)

        buttonLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        setupAuthStateListener()
    }

    private fun setupAuthStateListener() {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // User is signed out, redirect to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                updateUI(user)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (::authStateListener.isInitialized) {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: FirebaseUser) {
        val email = user.email ?: "No email"
        textViewEmail.text = email

        val displayName = user.displayName ?: email.split("@")[0]
        textViewGreeting.text = "Hello, $displayName!"
    }
}
