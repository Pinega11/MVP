package com.example.myapplication1.ui.neworderpages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication1.R
import com.example.myapplication1.ui.database.ConSQL
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class PageOneActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL
    private lateinit var storageRef: StorageReference
    private lateinit var imageFromDataBaseFirebase: String

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pageone)

        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val editTextFIO: EditText = findViewById(R.id.editTextOneFIO)
        val editTextPhone: EditText = findViewById(R.id.editTextOnePhone)
        val editTextAddress: EditText = findViewById(R.id.editTextOneAddress)
        val spinnerOne: Spinner = findViewById(R.id.spinnerOneFloor)
        val checkAvtoCar: CheckBox = findViewById(R.id.checkBoxOneCar)
        val checkGiloe: CheckBox = findViewById(R.id.checkBoxOneJiloe)
        val checkBitovoe: CheckBox = findViewById(R.id.checkBoxOneBitovoe)
        val checkOtdelka: CheckBox = findViewById(R.id.checkBoxOneOtdelka)
        val checkRemont: CheckBox = findViewById(R.id.checkBoxOneRemont)
        val buttonInside: ImageButton = findViewById(R.id.imageButtonOneInside)
        val buttonNextTwoPage: Button = findViewById(R.id.buttonNextTwoPage)

        val phoneForEditTest = prefs.getString("phone", null)
        if (phoneForEditTest != null) {
            val clientData = conSQL.getClientData(phoneForEditTest)
            if (clientData != null) {
                val fio = "${clientData.surname} ${clientData.name} ${clientData.patronomyc}"
                editTextFIO.setText(fio)
                editTextPhone.setText(clientData.phone)
                editTextAddress.setText(clientData.address)
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.floor_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOne.adapter = adapter
        }

        spinnerOne.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedValue = position + 1

                if (selectedValue in 3..7) {
                    checkAvtoCar.isChecked = true
                    checkAvtoCar.isEnabled = false
                } else {
                    checkAvtoCar.isChecked = false
                    checkAvtoCar.isEnabled = true
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        checkGiloe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBitovoe.isChecked = false
                checkBitovoe.isEnabled = false
            } else {
                checkBitovoe.isEnabled = true
            }
        }

        checkBitovoe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkGiloe.isChecked = false
                checkGiloe.isEnabled = false
            } else {
                checkGiloe.isEnabled = true
            }
        }

        checkOtdelka.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkRemont.isChecked = false
                checkRemont.isEnabled = false
            } else {
                checkRemont.isEnabled = true
            }
        }

        checkRemont.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkOtdelka.isChecked = false
                checkOtdelka.isEnabled = false
            } else {
                checkOtdelka.isEnabled = true
            }
        }

        buttonInside.setOnClickListener {
            openGallery()
        }

        buttonNextTwoPage.setOnClickListener {
            val phoneForEditTestFinal = prefs.getString("phone", null)
            val enteredPhone = editTextPhone.text.toString()

            if (phoneForEditTestFinal != enteredPhone) {
                Toast.makeText(this, getString(R.string.ErrorWithPhone), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (conSQL.checkOrderExistenceWithStatus(enteredPhone, "0")) {
                Toast.makeText(this, getString(R.string.NumberIsCreate), Toast.LENGTH_SHORT).show()
            } else {
                val fio = editTextFIO.text.toString()
                val address = editTextAddress.text.toString()
                val floor = spinnerOne.selectedItem.toString()
                val avtocar = if (checkAvtoCar.isChecked) "Да" else "Нет"
                val typeOfRoom = if (checkGiloe.isChecked) "Жилое помещение" else if (checkBitovoe.isChecked) "Бытовое помещение" else ""
                val conditionOfRoom = if (checkOtdelka.isChecked) "Отделка" else if (checkRemont.isChecked) "Ремонт" else ""
                val status = "0"

                if (fio.isEmpty() || enteredPhone.isEmpty() || address.isEmpty() ||
                    floor.isEmpty() || typeOfRoom.isEmpty() || conditionOfRoom.isEmpty()
                ) {
                    Toast.makeText(this, getString(R.string.FillOn), Toast.LENGTH_SHORT).show()
                } else {
                    conSQL.insertOrder(fio, enteredPhone, address, floor, avtocar, typeOfRoom, conditionOfRoom, status)

                    if (::imageFromDataBaseFirebase.isInitialized) {
                        val phoneForEditTestFinal = prefs.getString("phone", null)
                        uploadImageToFirebaseStorage(phoneForEditTestFinal)
                    }

                    val intent = Intent(this, PageTwoActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        imageFromDataBaseFirebase = selectedImageUri.toString()
                        Log.d("FirebaseStorage", "Image selected successfully: $imageFromDataBaseFirebase")
                    } else {
                        Log.e("FirebaseStorage", "Failed to select image")
                    }
                }
            }
        } else {
            Log.e("FirebaseStorage", "Failed to select image. Result code: $resultCode")
        }
    }

    private fun uploadImageToFirebaseStorage(phone: String?) {
        if (phone.isNullOrEmpty()) {
            Log.e("FirebaseStorage", "Phone number is null or empty.")
            return
        }

        val fileUri: Uri = Uri.parse(imageFromDataBaseFirebase)
        val file = File(fileUri.path)

        val photoRef = storageRef.child("photos/${file.name}")

        photoRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                photoRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    saveImageUrlToDatabase(phone, downloadUrl)
                }.addOnFailureListener { e ->
                    Log.e("FirebaseStorage", "Error getting download URL: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseStorage", "Error uploading file: ${e.message}")
            }
    }

    private fun saveImageUrlToDatabase(phone: String, imageUrlInside: String) {
        conSQL.updatePhotoUrlInside(phone, imageUrlInside)
    }
}