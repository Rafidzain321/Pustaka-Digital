package com.example.pustaka

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pustaka.welcome_screen.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // SharedPreferences untuk cek first launch & login state
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("isFirstLaunch", true)
        val isLogin = sharedPref.getBoolean("isLogin", false)

        lifecycleScope.launch {
            delay(2000) // Delay 2 detik buat SplashScreen

            if (isFirstLaunch) {
                // Kalau pertama kali buka aplikasi
                startActivity(Intent(this@SplashScreenActivity, WelcomeActivity::class.java))

                // Update SharedPreferences untuk tandai Welcome Screen udah pernah muncul
                with(sharedPref.edit()) {
                    putBoolean("isFirstLaunch", false)
                    apply()
                }
            } else {
                // Kalau bukan pertama kali buka aplikasi, cek isLogin
                if (isLogin) {
                    // Kalau user udah login, langsung ke Home
                    startActivity(Intent(this@SplashScreenActivity, Home::class.java))
                } else {
                    // Kalau belum login, ke LoginActivity
                    startActivity(Intent(this@SplashScreenActivity, Login::class.java))
                }
            }
            finish() // Tutup SplashScreen
        }
    }
}
