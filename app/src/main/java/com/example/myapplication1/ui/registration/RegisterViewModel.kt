package com.example.myapplication1.ui.registration

import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    fun validateRegisterInput(
        name: String,
        familia: String,
        otchestvo: String,
        phone: String,
        password: String,
        adres: String,
        email: String,
        date: String
    ): Boolean {
        return name.isNotBlank() &&
                familia.isNotBlank() &&
                otchestvo.isNotBlank() &&
                phone.isNotBlank() &&
                password.isNotBlank() &&
                adres.isNotBlank() &&
                email.isNotBlank() &&
                date.isNotBlank()
    }
}
