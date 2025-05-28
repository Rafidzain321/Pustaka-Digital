package com.example.pustaka

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pustaka.admin.AdminActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        if (sharedPreferences.getBoolean("isLogin", false)) {
            val email = sharedPreferences.getString("email", "")
            if (email == "admin@gmail.com") {
                startActivity(Intent(this, AdminActivity::class.java))
            } else {
                startActivity(Intent(this, Home::class.java))
            }
            finish()
        }

        val signIn: Button = findViewById(R.id.sign_in)
        val usn: EditText = findViewById(R.id.usn)
        val pass: EditText = findViewById(R.id.pw)
        val signUpTextView: TextView = findViewById(R.id.sign_up)
        val btnBack: ImageView = findViewById(R.id.imageBack)

        signIn.setOnClickListener {
            val email = usn.text.toString().trim()
            val password = pass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showSnackbar("Silahkan isi email dan password.")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {

                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLogin", true)
                        editor.putString("email", email)
                        editor.apply()

                        if (email == "admin@gmail.com") {
                            startActivity(Intent(this, AdminActivity::class.java))
                        } else {
                            startActivity(Intent(this, Home::class.java))
                        }
                        finish()
                    }
                } else {
                    showSnackbar("Login gagal: ${task.exception?.message}")
                }
            }
        }

        signUpTextView.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showSnackbar(message: String) {
        val view = this.findViewById<View>(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}
