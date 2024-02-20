package com.example.ctvms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.Plans
import com.example.ctvms.databinding.FragmentPlanViewBinding

class plansAdapter(private var plans: List<Plans>, private val itemClickListener: OnPlanItemClickListener) : RecyclerView.Adapter<plansAdapter.PlanViewHolder>() {

    interface OnPlanItemClickListener {
        fun onPlanItemClicked(planId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = FragmentPlanViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.bind(plan)
        holder.itemView.setOnClickListener {
            itemClickListener.onPlanItemClicked(plan.fullid)
        }
    }

    override fun getItemCount(): Int {
        return plans.size
    }

    fun setData(newPlansList: List<Plans>) {
        plans = newPlansList
        notifyDataSetChanged()
    }

    inner class PlanViewHolder(private val binding: FragmentPlanViewBinding) :
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
