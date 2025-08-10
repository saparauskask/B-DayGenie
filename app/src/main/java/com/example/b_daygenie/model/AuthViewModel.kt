package com.example.b_daygenie.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.b_daygenie.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    private val userLiveData: MutableLiveData<FirebaseUser?> = authRepository.getUserLiveData()

    fun login(email: String, password: String) {
        authRepository.login(email, password)
    }

    fun register(email: String, password: String, repeatPassword: String) {
        authRepository.register(email, password, repeatPassword)  }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }
}