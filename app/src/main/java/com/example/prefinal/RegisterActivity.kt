package com.example.prefinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val confirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        val loginText = findViewById<TextView>(R.id.textViewLogin)

        registerButton.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirmPassword.text.toString()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty() && confirmPasswordText.isNotEmpty()) {
                if (passwordText.length < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                if (passwordText == confirmPasswordText) {
                    registerButton.isEnabled = false
                    registerButton.text = "Creating account..."
                    
                    auth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(this) { task ->
                            registerButton.isEnabled = true
                            registerButton.text = "Register"
                            
                            if (task.isSuccessful) {
                                // Registration success, navigate to MainActivity
                                Toast.makeText(baseContext, "Registration successful!",
                                    Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                // If registration fails, display a message to the user.
                                val errorMessage = task.exception?.message ?: "Registration failed."
                                Toast.makeText(baseContext, errorMessage,
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}