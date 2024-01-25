package com.example.myapplication1.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication1.R
import com.example.myapplication1.databinding.ActivityMainBinding
import com.example.myapplication1.ui.database.ConSQL

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        updateLoginMenuItem(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                invalidateOptionsMenu()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateLoginMenuItem(menu: Menu?) {
        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        val textViewFIO: TextView? = findViewById(R.id.action_login)
        val phone = prefs.getString("phone", null)
        if (phone != null) {
            val clientFIO = conSQL.getClientFIO(phone)
            if (clientFIO != null) {
                if (textViewFIO != null) {
                    textViewFIO.text = clientFIO
                }

                val loginMenuItem = menu?.findItem(R.id.action_login)
                loginMenuItem?.title = clientFIO
            }
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_order, R.id.navigation_neworder, R.id.navigation_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}