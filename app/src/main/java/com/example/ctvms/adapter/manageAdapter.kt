package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.databinding.FragmentManageBinding
import com.example.ctvms.databinding.ManagecustomersviewBinding
import com.example.ctvms.databinding.RecenttransactionsviewBinding

class manageAdapter(private val names:List<String>,private val crfnos:List<String>,private val avatars:List<Int>) :RecyclerView.Adapter<manageAdapter.ManageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        return manageAdapter.ManageViewHolder(ManagecustomersviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {
        val name = names[position]
        val crfno = crfnos[position]
        val avatar = avatars[position]
        holder.bind(name,crfno,avatar)
    }
    override fun getItemCount(): Int {
        return names.size
    }
    class ManageViewHolder(private val binding: ManagecustomersviewBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(name:String,crfno:String,avatar:Int){
            binding.CusName.text = name
            binding.crfno.text = crfno
            binding.cusavatar.setImageResource(avatar)
        }
    }
}