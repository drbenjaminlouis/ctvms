package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.Customer
import com.example.ctvms.databinding.ManagecustomersviewBinding

class manageAdapter(private var customers: List<Customer>) : RecyclerView.Adapter<manageAdapter.ManageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        val binding = ManagecustomersviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {
        val customer = customers[position]
        holder.bind(customer)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    class ManageViewHolder(private val binding: ManagecustomersviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Customer) {
            binding.CusName.text = customer.name
            binding.crfno.text = customer.crfNo
            binding.cusavatar.setImageResource(customer.avatarResourceId)
        }
    }
    fun updateData(newCustomers: List<Customer>) {
        customers = newCustomers
        notifyDataSetChanged() // Notify RecyclerView that the data has changed
    }
}