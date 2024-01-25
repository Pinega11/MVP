package com.example.myapplication1.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication1.databinding.FragmentMenuBinding
import com.example.myapplication1.ui.database.ConSQL
import com.example.myapplication1.ui.login.LoginActivity
import com.example.myapplication1.ui.settings.parameters.SettingsActivity
import java.util.Locale

class MenuFragment : Fragment() {

    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)

        prefs = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonExit: Button = binding.buttonExit
        buttonExit.setOnClickListener {
            prefs.edit().clear().apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val buttonSettings: Button = binding.buttonSettings
        buttonSettings.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}