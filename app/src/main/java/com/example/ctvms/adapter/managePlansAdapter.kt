package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.Plans
import com.example.ctvms.databinding.FragmentManagePlanViewBinding

class managePlansAdapter(
    private var plans: List<Plans>,
) : RecyclerView.Adapter<managePlansAdapter.ManagePlanViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagePlanViewHolder {
        val binding = FragmentManagePlanViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManagePlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManagePlanViewHolder, position: Int) {
        val plans = plans[position]
        holder.bind(plans)
    }

    override fun getItemCount(): Int {
        return plans.size
    }

    fun setData(newPlansList: List<Plans>) {
        plans = newPlansList
        notifyDataSetChanged()
    }

    class ManagePlanViewHolder(private val binding: FragmentManagePlanViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: Plans) {
            binding.planIDVal.text = plan.planID
            binding.planNameValue.text = plan.planName
            binding.planChannelValue.text = plan.channelCount
            binding.planIcon.setImageResource(plan.planIcon)
            binding.planPiceValue.text = plan.planPrice
        }
    }
}
