package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.databinding.FragmentReportscustomerviewBinding
import com.example.ctvms.databinding.FragmentTransactionscustomerviewBinding

class transactionsAdapter(private val Names:List<String>,private val crfnos:List<String>,private val avatars:List<Int>,private val amounts:List<String>): RecyclerView.Adapter<transactionsAdapter.TransactionsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        return transactionsAdapter.TransactionsViewHolder(FragmentTransactionscustomerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val name = Names[position]
        val avatar = avatars[position]
        val crfno = crfnos[position]
        val amount = amounts[position]
        holder.bind(name,crfno,avatar,amount)
    }
    override fun getItemCount(): Int {
        return Names.size
    }
    class TransactionsViewHolder(private val binding: FragmentTransactionscustomerviewBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(name:String,crfno:String,avatar:Int,amount:String){
            binding.custname.text = name
            binding.crfnotext.text = crfno
            binding.custavatar.setImageResource(avatar)
            binding.amounttext.text = amount
        }

    }
}