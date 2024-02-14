package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.databinding.FragmentTransactionscustomerviewBinding
import com.example.ctvms.databinding.FragmentTransactionviewBinding
import com.example.ctvms.databinding.FragmentTransactionsBinding

// CustomerData data model
data class CustomerData(val name: String, val crfNo: String, val avatar: Int, val amount: String)

// Adapter for individual transactions
class TransactionsAdapter(private val customerDataList: List<CustomerData>) :
    RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentTransactionscustomerviewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = customerDataList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return customerDataList.size
    }

    // ViewHolder implementation
    inner class ViewHolder(private val binding: FragmentTransactionscustomerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customerData: CustomerData) {
            // Bind your data to the ViewHolder's views here
            binding.custname.text = customerData.name
            binding.crfnotext.text = customerData.crfNo
            binding.custavatar.setImageResource(customerData.avatar)
            binding.amounttext.text = customerData.amount
            // Set other views accordingly
        }
    }
}

// Adapter for the main transactions fragment
class TransactionsmainAdapter(private val dateLabel: List<String>, private val customerDataList: List<List<CustomerData>>) :
    RecyclerView.Adapter<TransactionsmainAdapter.TransactionMainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionMainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentTransactionviewBinding.inflate(inflater, parent, false)
        return TransactionMainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionMainViewHolder, position: Int) {
        val dateText = dateLabel[position]
        val customerData = customerDataList[position]
        holder.bind(dateText, customerData)
    }

    override fun getItemCount(): Int {
        return dateLabel.size
    }

    class TransactionMainViewHolder(private val binding: FragmentTransactionviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dateText: String, customerData: List<CustomerData>) {
            binding.dateLabel.text = dateText
            val adapter = TransactionsAdapter(customerData)
            binding.transactionsRecycle.layoutManager = LinearLayoutManager(binding.root.context)
            binding.transactionsRecycle.adapter = adapter
        }
    }
}
