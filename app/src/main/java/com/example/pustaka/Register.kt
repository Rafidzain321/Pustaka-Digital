package com.example.pustaka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ambil View dari layout
        val emailField: EditText = findViewById(R.id.usn)
        val passwordField: EditText = findViewById(R.id.pw)
        val confirmPasswordField: EditText = findViewById(R.id.cpw)
        val registerButton: Button = findViewById(R.id.sign_up)
        val signInText: TextView = findViewById(R.id.sign_in)
        val backButton: ImageView = findViewById(R.id.imageBack)

        // Event tombol register
        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Semua kolom harus diisi!")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showToast("Password tidak cocok!")
                return@setOnClickListener
            }

            // Register user di Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Registrasi berhasil! Silakan login.")
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    } else {
                        showToast("Registrasi gagal: ${task.exception?.message}")
                    }
                }
        }

        // Redirect ke Login
        signInText.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        // Tombol kembali
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
