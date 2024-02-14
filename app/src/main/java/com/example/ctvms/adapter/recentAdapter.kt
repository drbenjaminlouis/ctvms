package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.databinding.RecenttransactionsviewBinding

class recentAdapter(private val Names:List<String>,private val avatars:List<Int>,private val crfnos:List<String>,private val amounts:List<String>):RecyclerView.Adapter<recentAdapter.RecentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        return RecentViewHolder(RecenttransactionsviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val name = Names[position]
        val avatar = avatars[position]
        val crfno = crfnos[position]
        val amount = amounts[position]
        holder.bind(name,avatar,crfno, amount)
    }
    override fun getItemCount(): Int {
        return Names.size
    }
    class RecentViewHolder(private val binding: RecenttransactionsviewBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(name:String, avatar:Int, crfno:String, amount: String){
            binding.custname.text = name
            binding.crfnotext.text = crfno
            binding.custavatar.setImageResource(avatar)
            binding.amounttext.text = amount
        }
    }
}