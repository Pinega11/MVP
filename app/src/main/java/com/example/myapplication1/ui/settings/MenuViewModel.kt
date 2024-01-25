package com.example.myapplication1.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Окно для меню"
    }

    val text: LiveData<String> = _text
}