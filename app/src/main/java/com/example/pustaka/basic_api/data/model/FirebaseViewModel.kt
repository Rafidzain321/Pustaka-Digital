package com.example.caffe_app.basic_api.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pustaka.basic_api.data.model.User
import com.example.pustaka.basic_api.data.repository.FirebaseRepository
import com.example.pustaka.basic_api.utils.Resource
import kotlinx.coroutines.launch

class FirebaseViewModel(private val repository: FirebaseRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<User>>()
    val loginState: LiveData<Resource<User>> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = Resource.Loading() // Set state ke Loading

        viewModelScope.launch {
            try {
                val user = repository.login(email, password)
                if (user!=null) {
                    _loginState.value = Resource.Success(user)
                } else {
                    _loginState.value = Resource.Error("Login gagal. Periksa kembali kredensial Anda.")
                }
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "Terjadi kesalahan saat login")
            }
        }
    }

    fun getCurrentUser() = repository.getCurrentUser()
}