package com.example.myapplication1.ui.registration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication1.R
import com.example.myapplication1.databinding.ActivityRegisterBinding
import com.example.myapplication1.ui.database.ConSQL
import com.example.myapplication1.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var conSQL: ConSQL

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        conSQL = ConSQL()

        val editTextFamilia: EditText = findViewById(R.id.editTextSurname)
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextOtchestvo: EditText = findViewById(R.id.editTextPatronymic)
        val spinner: Spinner = findViewById(R.id.spinnerGender)
        val editTextPhone: EditText = findViewById(R.id.editTextRegPhone)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextAddress: EditText = findViewById(R.id.editTextTextPostalAddress)
        val editTextEmail: EditText = findViewById(R.id.editTextMail)

        val registerButton: Button = findViewById(R.id.buttonRegister)
        val loginTextView: TextView = findViewById(R.id.textViewLogin)

        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        registerButton.setOnClickListener {
            val familia = editTextFamilia.text.toString()
            val name = editTextName.text.toString()
            val otchestvo = editTextOtchestvo.text.toString()
            val gender = spinner.selectedItem.toString()
            val phone = editTextPhone.text.toString()
            val password = editTextPassword.text.toString()
            val address = editTextAddress.text.toString()
            val email = editTextEmail.text.toString()

            if (familia.isEmpty() || name.isEmpty() || otchestvo.isEmpty() ||
                phone.isEmpty() || password.isEmpty() || address.isEmpty() || email.isEmpty()
            ) {
                Toast.makeText(this, getString(R.string.AllFillsMustBe), Toast.LENGTH_SHORT).show()
            } else {
                val conSQL = ConSQL()
                conSQL.insertClient(familia, name, otchestvo, gender, phone, password, address, email)
                Toast.makeText(this, getString(R.string.RegisterYes), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginTextView.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
