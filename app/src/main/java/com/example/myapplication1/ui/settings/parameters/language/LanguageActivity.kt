package com.example.myapplication1.ui.settings.parameters.language

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication1.R
import com.example.myapplication1.ui.database.ConSQL
import com.example.myapplication1.ui.settings.parameters.SettingsActivity
import java.util.Locale

class LanguageActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        val buttonToRussian: Button = findViewById(R.id.buttonSetRussian)
        val buttonToEnglish: Button = findViewById(R.id.buttonSetEnglish)

        val buttonSave: Button = findViewById(R.id.buttonSaveLanguage)


        buttonToRussian.setOnClickListener {
            changeLanguage("en")
        }

        buttonToEnglish.setOnClickListener {
            changeLanguage("ru")
        }

        buttonSave.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changeLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)

        val context: Context = baseContext
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

        val editor = prefs.edit()
        editor.putString("language", languageCode)
        editor.apply()

        recreate()
    }
}