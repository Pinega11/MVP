package com.example.myapplication1.ui.settings.parameters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication1.R
import com.example.myapplication1.ui.MainActivity
import com.example.myapplication1.ui.newOrder.NewOrderFragment
import com.example.myapplication1.ui.order.OrderFragment
import com.example.myapplication1.ui.settings.MenuFragment
import com.example.myapplication1.ui.settings.parameters.language.LanguageActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonLanguage: Button = findViewById(R.id.buttonParamLanguage)

        buttonLanguage.setOnClickListener {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}