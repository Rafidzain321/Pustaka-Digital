package com.example.pustaka

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pustaka.basic_listview.ListViewActivity
import com.example.pustaka.basic_recycleview.RecyclerViewActivity
import com.example.pustaka.welcome_screen.WelcomeActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tombol lainnya tetap ada, tetapi tidak memuat fragment lain di MainActivity
        val btnsignin: Button = findViewById(R.id.signin)
        btnsignin.setOnClickListener {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }


        val btnListView: Button = findViewById(R.id.btnListView)
        btnListView.setOnClickListener {
            val i = Intent(this, ListViewActivity::class.java)
            startActivity(i)
        }

        val btnRecycleView: Button = findViewById(R.id.btnRecycleView)
        btnRecycleView.setOnClickListener {
            val i = Intent(this, RecyclerViewActivity::class.java)
            startActivity(i)
        }

        // Memulai Home sebagai tampilan utama jika pengguna sudah login
        checkLoginAndStartHome()
    }

    private fun checkLoginAndStartHome() {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean("isLogin", false)

        if (isLogin) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish() // Tutup MainActivity setelah Home terbuka
        }
    }

    private fun showSnackbar(message: String) {
        val view = this.findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}