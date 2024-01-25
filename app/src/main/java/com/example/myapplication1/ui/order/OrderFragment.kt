package com.example.myapplication1.ui.order

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication1.R
import com.example.myapplication1.databinding.FragmentOrderBinding
import com.example.myapplication1.ui.database.ConSQL

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private lateinit var prefs: SharedPreferences
    private lateinit var conSQL: ConSQL
    private lateinit var orderView: ListView
    private lateinit var ordersList: List<ConSQL.Order>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(OrderViewModel::class.java)

        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()

        val textView: TextView = binding.textOrder
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        orderView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedOrder = ordersList[position]
            showDeleteConfirmationDialog(selectedOrder)
            true
        }

        return root
    }

    private fun initViews() {
        prefs = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        conSQL = ConSQL()
        orderView = binding.orderView

        val phone = prefs.getString("phone", null)

        if (phone != null) {
            ordersList = conSQL.getOrdersDataByPhone(phone)
            val adapter = OrderAdapter(requireContext(), R.layout.order_list_item, ordersList)
            orderView.adapter = adapter
        }
    }

    private class OrderAdapter(
        context: Context,
        resource: Int,
        objects: List<ConSQL.Order>
    ) : ArrayAdapter<ConSQL.Order>(context, resource, objects) {

        @SuppressLint("SetTextI18n", "ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val itemView = inflater.inflate(R.layout.order_list_item, parent, false)

            val item = getItem(position)
            if (item != null) {
                val textFio: TextView = itemView.findViewById(R.id.textFio)
                val textPhone: TextView = itemView.findViewById(R.id.textPhone)
                val textAddress: TextView = itemView.findViewById(R.id.textAddress)
                val textConditioner: TextView = itemView.findViewById(R.id.textConditioner)
                val textCost: TextView = itemView.findViewById(R.id.textCost)
                val textStatus: TextView = itemView.findViewById(R.id.textStatus)

                textFio.text = context.getString(R.string.NumberOrder, item.number_order)
                textPhone.text = context.getString(R.string.PhoneOrder, item.phone)
                textAddress.text = context.getString(R.string.AddressOrder, item.address)
                textConditioner.text = context.getString(R.string.ConderOrder, item.conditioner)
                textCost.text = context.getString(R.string.CostFinal, item.cost_of_work )
                textStatus.text = context.getString(R.string.StatusOrder, getStatusText(item.status))
            }

            return itemView
        }

        private fun getStatusText(status: String): String {
            return when (status) {
                "0" -> context.getString(R.string.rassmotrenie)
                "1" -> context.getString(R.string.Rassmotrena)
                "2" -> context.getString(R.string.Potdverzdena)
                else -> context.getString(R.string.ErrorMessage)
            }
        }
    }

    private fun showDeleteConfirmationDialog(order: ConSQL.Order) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.DeleteOrder))
            .setMessage(getString(R.string.SureDelete, order.number_order))
            .setPositiveButton(getString(R.string.ExitYes)) { _, _ ->
                deleteOrder(order)
            }
            .setNegativeButton(getString(R.string.ExitNo)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteOrder(order: ConSQL.Order) {
        val phone = prefs.getString("phone", null)
        if (phone != null) {
            conSQL.deleteOrder(phone, order.number_order)
            ordersList = conSQL.getOrdersDataByPhone(phone)
            val adapter = OrderAdapter(requireContext(), R.layout.order_list_item, ordersList)
            orderView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
