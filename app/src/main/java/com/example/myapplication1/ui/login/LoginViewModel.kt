package com.example.myapplication1.ui.login

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun validateLoginInput(phone: String, password: String): Boolean {

        return phone.isNotBlank() && password.isNotBlank()
    }
}