package com.example.myapplication1.ui.neworderpages

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.R
import com.example.myapplication1.databinding.ActivityPagethreeBinding
import com.example.myapplication1.ui.MainActivity
import com.example.myapplication1.ui.database.ConSQL

class PageThreeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagethreeBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL
    private lateinit var spinnerConder: Spinner

    private lateinit var costConder: TextView

    private var buttonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagethreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        val sqaureroom: TextView = findViewById(R.id.textViewSquareS)
        costConder = findViewById(R.id.textViewCostS)

        spinnerConder = findViewById(R.id.spinnerConder)
        val btnNextFourPage: Button = findViewById(R.id.buttonNextFourPage)

        val phone = prefs.getString("phone", null)

        if (phone != null) {

            val squareValueFromDatabase = conSQL.getSquareFromDatabase(phone)?.replace(',', '.')?.toDoubleOrNull()

            if (squareValueFromDatabase != null) {
                sqaureroom.text = squareValueFromDatabase.toString()
                Log.d("PageThreeActivity", "Received square_of_room from database: $squareValueFromDatabase")

                val conditionersList = conSQL.getConditionersBySquare(squareValueFromDatabase.toDouble())

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditionersList.map { "${it.firma} ${it.model}" })
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerConder.adapter = adapter
            } else {
                Log.d("PageThreeActivity", "Square_of_room not found in database")
            }
        }

        btnNextFourPage.setOnClickListener {
            buttonClicked = true
            val selectedConditioner = spinnerConder.selectedItem.toString()
            val phone = prefs.getString("phone", null)

            val cost_of_work = phone?.let { conSQL.getCostOfWorkFromDatabase(it) }?.toIntOrNull()
            val cost_of_conder = costConder.text.toString().toIntOrNull()

            if (cost_of_work != null && cost_of_conder != null) {
                val totalCost = cost_of_work + cost_of_conder
                val summa = totalCost.toString()

                if (phone != null) {
                    conSQL.updateOrderConder(phone, selectedConditioner)
                    conSQL.updateOrderCost(phone, summa)
                }

                val intent = Intent(this, PageFourActivity::class.java)
                startActivity(intent)
            }
        }

        spinnerConder.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedConditioner = spinnerConder.selectedItem.toString()
                val cost = conSQL.getConditionerCost(selectedConditioner)

                costConder.text = "$cost"
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })
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
