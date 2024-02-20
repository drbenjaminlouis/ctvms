package com.example.ctvms.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ctvms.R
import com.example.ctvms.adapter.recentAdapter
import com.example.ctvms.addCustomer
import com.example.ctvms.databinding.FragmentHomeBinding
import com.example.ctvms.dueChecker

class adminhomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isOriginalState = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val add_user = binding.newuserlayout
        val due_checker = binding.duecheckerlayout
        add_user.setOnClickListener(){
            val intent = Intent(requireContext(), addCustomer::class.java)
            startActivity(intent)
        }
        due_checker.setOnClickListener(){
            val intent = Intent(requireContext(), dueChecker::class.java)
            startActivity(intent)
        }
        setupTouchListeners(
            binding.totalcustomerlayout, binding.totalcusicon, binding.totalcustlabel,
            R.drawable.totalcusicon, "100", "Total Customers"
        )
        setupTouchListeners(
            binding.activecustomerlayout, binding.activeicon, binding.activelabel,
            R.drawable.totalcusicon, "80", "Active Customers"
        )
        setupTouchListeners(
            binding.inactivecustomerlayout, binding.inactiveicon, binding.inactivelabel,
            R.drawable.inactiveicon, "20", "Inactive Customers"
        )
        setupTouchListeners(
            binding.renewcustomerlayout, binding.renewalsicon, binding.renewalslabel,
            R.drawable.renewalsicon, "10", "Renewals"
        )
        setupTouchListeners(
            binding.receviedlayout, binding.receivedicon, binding.receivedlabel,
            R.drawable.receivedicon, "100", "Received Amount"
        )
        setupTouchListeners(
            binding.pendinglayout, binding.pendingicon, binding.pendinglabel,
            R.drawable.pendingicon, "100", "Inactive Customers"
        )
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListeners(
        layout: LinearLayout, icon: ImageView, label: TextView,
        iconResource: Int, newText: String, originalText: String
    ) {
        layout.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (isOriginalState) {
                        icon.setImageResource(0)
                        label.text = newText
                        isOriginalState = false
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!isOriginalState) {
                        icon.setImageResource(iconResource)
                        label.text = originalText
                        isOriginalState = true
                    }
                }
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Cusname = listOf("John Doe", "Jane Smith", "Aby Jose", "Anna Rose", "John Doe", "John Doe")
        val Cuscrfno = listOf("000100", "000105", "000110", "000102", "000100", "000100")
        val custAvatar = listOf(R.drawable.avatar3, R.drawable.avatar, R.drawable.avatar4, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar3)
        val CusAmounts = listOf("+1000", "+500", "+250", "+750", "+500", "+500")
        val adapter = recentAdapter(Cusname, custAvatar, Cuscrfno, CusAmounts)
        binding.recenttransrecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recenttransrecycler.adapter = adapter
    }

    companion object {
    }
}
