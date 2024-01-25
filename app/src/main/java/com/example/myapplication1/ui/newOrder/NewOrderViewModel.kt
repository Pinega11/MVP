package com.example.myapplication1.ui.newOrder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewOrderViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "Создать новую заявку"
    }
    val text: LiveData<String> = _text

}