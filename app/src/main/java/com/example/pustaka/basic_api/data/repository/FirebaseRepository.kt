package com.example.pustaka.basic_api.data.repository

import com.example.pustaka.basic_api.data.firebase.FirebaseAuthService
import com.example.pustaka.basic_api.data.model.User

class FirebaseRepository(private val authService: FirebaseAuthService) {
    suspend fun login(email: String, password: String): User? {
        val authResult = authService.login(email, password)
        if (authResult != null) {
            // Mapping Firebase user data to UserResponse
            return User(
                id = null,
                name = authResult.displayName ?: "Unknown", // Gunakan nama pengguna dari Firebase
                username = authResult.email?.substringBefore('@') ?: "Unknown", // Buat username sederhana
                email = authResult.email ?: "Unknown"
            )
        }
        return null
    }

    suspend fun register(email: String, password: String): Boolean {
        return authService.register(email, password)
    }

    fun getCurrentUser() = authService.getCurrentUser()

}