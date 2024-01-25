package com.example.myapplication1.ui.neworderpages

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.R
import com.example.myapplication1.databinding.ActivityPagefourBinding
import com.example.myapplication1.ui.MainActivity
import com.example.myapplication1.ui.database.ConSQL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class PageFourActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagefourBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL

    private var buttonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagefourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        val editTextFIO: EditText = findViewById(R.id.editTextFIOFinal)
        val editTextPhone: EditText = findViewById(R.id.editTextPhoneFinal)
        val editTextAddress: EditText = findViewById(R.id.editTextAddressFinal)
        val textViewFloor: TextView = findViewById(R.id.textViewFloorFinal)
        val textViewAvtocar: TextView = findViewById(R.id.textViewCarFinal)
        val textViewSquare: TextView = findViewById(R.id.textViewSquareFinal)
        val textViewType: TextView = findViewById(R.id.textViewTypeFinal)
        val textViewSost: TextView = findViewById(R.id.textViewComditionFinal)
        val textViewConder: TextView = findViewById(R.id.textViewConderFinal)
        val textViewCost: TextView = findViewById(R.id.textViewCostFinal)
        val buttonFinal: Button = findViewById(R.id.buttonFinal)

        val phoneForEditTestFinal = prefs.getString("phone", null)
        if (phoneForEditTestFinal != null) {
            val orderData = conSQL.getOrderData(phoneForEditTestFinal)
            if (orderData != null) {

                editTextFIO.setText(orderData.fio)
                editTextPhone.setText(orderData.phone)
                editTextAddress.setText(orderData.address)
                textViewFloor.text = orderData.floors
                textViewAvtocar.text = orderData.avtocar
                textViewSquare.text = orderData.square_of_room
                textViewType.text = orderData.type_of_room
                textViewSost.text = orderData.condition_of_room
                textViewConder.text = orderData.conditioner
            }
        }

        val phone = prefs.getString("phone", null)
        val sum = phone?.let { conSQL.getCostOfWorkFromDatabase(it) }
        textViewCost.text = sum.toString()

        buttonFinal.setOnClickListener{
            buttonClicked = true
            val randomNumber = generateRandomNumber()
            val phone = prefs.getString("phone", null)

            if (phone != null) {
                val date_of_reg_application = getCurrentDate()
                conSQL.updateOrder(phone, randomNumber, date_of_reg_application)
            }


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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

    fun generateRandomNumber(): String {
        val random = Random()
        val randomNumber = random.nextInt(90000) + 10000
        return randomNumber.toString()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date_of_reg_application = Date()
        return dateFormat.format(date_of_reg_application)
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
