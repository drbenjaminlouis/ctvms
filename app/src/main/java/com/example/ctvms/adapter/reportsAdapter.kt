package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.R
import com.example.ctvms.databinding.FragmentReportsBinding
import com.example.ctvms.databinding.FragmentReportscustomerviewBinding
import com.example.ctvms.databinding.RecenttransactionsviewBinding

class reportsAdapter(private val Names:List<String>,private val avatars:List<Int>,private val crfnos:List<String>,private val status:List<String>): RecyclerView.Adapter<reportsAdapter.RecentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        return reportsAdapter.RecentViewHolder(FragmentReportscustomerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val name = Names[position]
        val avatar = avatars[position]
        val crfno = crfnos[position]
        val status = status[position]
        holder.bind(name,avatar,crfno, status)
    }
    override fun getItemCount(): Int {
        return Names.size
    }
    class RecentViewHolder(private val binding: FragmentReportscustomerviewBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(name:String, avatar:Int, crfno:String, status: String){
            binding.custname.text = name
            binding.crfnotext.text = crfno
            binding.custavatar.setImageResource(avatar)
            binding.statusText.text = status
            if (binding.statusText.text == "PAID") {
                // Change text color when the text is "PAID"
                binding.statusText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.paidTextColor))
            }else{
                binding.statusText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.pendingTextColor))
            }
        }

    }
}