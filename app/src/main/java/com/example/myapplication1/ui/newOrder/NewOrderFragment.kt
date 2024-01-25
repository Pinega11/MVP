package com.example.myapplication1.ui.newOrder

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication1.databinding.FragmentNeworderBinding
import com.example.myapplication1.ui.neworderpages.PageOneActivity

class NewOrderFragment : Fragment() {

    private var _binding: FragmentNeworderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val newOrderViewModel = ViewModelProvider(this).get(NewOrderViewModel::class.java)

        _binding = FragmentNeworderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val button: Button = binding.buttonNewOrder
        newOrderViewModel.text.observe(viewLifecycleOwner) {
            button.text = it
            button.gravity = Gravity.CENTER
        }

        button.setOnClickListener {
            val intent = Intent(requireContext(), PageOneActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
