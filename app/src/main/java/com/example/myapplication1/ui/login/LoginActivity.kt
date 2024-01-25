package com.example.myapplication1.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication1.R
import com.example.myapplication1.databinding.ActivityLoginBinding
import com.example.myapplication1.ui.MainActivity
import com.example.myapplication1.ui.database.ConSQL
import com.example.myapplication1.ui.registration.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var conSQL: ConSQL
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        conSQL = ConSQL()
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val phoneEditText: EditText = findViewById(R.id.editTextPhone)
        val passwordEditText: EditText = findViewById(R.id.editTextTextPassword)

        val savedPhone = sharedPreferences.getString("phone", null)
        val savedPassword = sharedPreferences.getString("password", null)

        if (savedPhone != null && savedPassword != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.button.setOnClickListener {
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (conSQL.loginUser(phone, password)) {
                with(sharedPreferences.edit()) {
                    putString("phone", phone)
                    putString("password", password)
                    apply()
                }

                Toast.makeText(this, getString(R.string.LoginYes), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.Error), Toast.LENGTH_SHORT).show()
            }
        }

        binding.register.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val desiredPrefix = "+7"

                if (s != null && !s.startsWith(desiredPrefix)) {
                    s.insert(0, desiredPrefix)
                }
            }
        })
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}