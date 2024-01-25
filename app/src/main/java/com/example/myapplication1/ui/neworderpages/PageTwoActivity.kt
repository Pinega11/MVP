package com.example.myapplication1.ui.neworderpages

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.R
import com.example.myapplication1.databinding.ActivityPagetwoBinding
import com.example.myapplication1.ui.MainActivity
import com.example.myapplication1.ui.database.ConSQL

class PageTwoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagetwoBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL
    private lateinit var editTextLong: EditText
    private lateinit var editTextShort: EditText
    private lateinit var textViewSquare: TextView
    private lateinit var textViewConder: TextView

    private var buttonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagetwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        editTextLong = findViewById(R.id.editTextLong)
        editTextShort = findViewById(R.id.editTextShort)
        textViewSquare = findViewById(R.id.textViewSquare)
        textViewConder = findViewById(R.id.textViewConder)

        val btnNextThreePage: Button = findViewById(R.id.buttonNextThreePage)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                calculateAndDisplaySquare()
            }

            override fun afterTextChanged(editable: Editable?) {
                val longText = editTextLong.text.toString()
                val shortText = editTextShort.text.toString()
                btnNextThreePage.isEnabled = longText.isNotEmpty() && shortText.isNotEmpty()
            }
        }

        editTextLong.addTextChangedListener(textWatcher)
        editTextShort.addTextChangedListener(textWatcher)

        btnNextThreePage.setOnClickListener {
            buttonClicked = true
            val square_of_room = textViewSquare.text.toString()
            val phone = prefs.getString("phone", null)

            if (editTextLong.text.toString().isEmpty() || editTextShort.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.FillOn), Toast.LENGTH_SHORT).show()
            } else {
                buttonClicked = true
                val square_of_room = textViewSquare.text.toString()
                val phone = prefs.getString("phone", null)

                if (phone != null) {
                    conSQL.updateOrderSquare(phone, square_of_room)
                }

                val intent = Intent(this, PageThreeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!buttonClicked) {
            showExitConfirmationDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage(getString(R.string.ExitNotFinish))
        alertDialogBuilder.setPositiveButton(getString(R.string.ExitYes)) { _, _ ->
            val phone = prefs.getString("phone", null)
            if (phone != null) {
                conSQL.deleteOrderButton(phone)
            }
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        alertDialogBuilder.setNegativeButton(getString(R.string.ExitNo)) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun calculateAndDisplaySquare() {
        val longValue = editTextLong.text.toString().toDoubleOrNull() ?: 0.0
        val shortValue = editTextShort.text.toString().toDoubleOrNull() ?: 0.0
        val square = longValue * shortValue

        val formattedSquare = String.format("%.1f", square)
        textViewSquare.text = "$formattedSquare"

        val conderInfo = conSQL.getConderBySquare(square)
        if (conderInfo != null) {
            val conderText = "${conderInfo.firma} ${conderInfo.model}"
            textViewConder.text = conderText
        } else {
            textViewConder.text = getString(R.string.NotFind)
        }
    }

    override fun onStop() {
        super.onStop()

        if (!buttonClicked) {
            deleteOrder()
        }
    }

    private fun deleteOrder() {
        val phone = prefs.getString("phone", null)
        if (phone != null) {
            conSQL.deleteOrderButton(phone)
        }
    }
}