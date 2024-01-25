package com.example.myapplication1.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()

    // Метод для обновления текста в зависимости от наличия заявок
    fun updateText(hasOrders: Boolean) {
        _text.value = if (hasOrders) {
            ""
        } else {
            "Заявки отсутствуют"
        }
    }

    val text: LiveData<String> = _text
}