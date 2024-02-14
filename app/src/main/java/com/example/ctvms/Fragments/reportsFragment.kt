package com.example.ctvms.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ctvms.R
import com.example.ctvms.adapter.manageAdapter
import com.example.ctvms.adapter.reportsAdapter
import com.example.ctvms.databinding.FragmentManageBinding
import com.example.ctvms.databinding.FragmentReportsBinding


class reportsFragment : Fragment() {
    private lateinit var binding: FragmentReportsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportsBinding.inflate(inflater, container, false)
        val yearID = binding.yearSpinner
        val MonthID = binding.monthSpinner
        val statusID = binding.statusSpinner
        val formatID = binding.formatSpinner
        val yearValues = arrayOf("Select","2023", "2024")
        val monthValues = arrayOf("Select","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val statusValues = arrayOf("Select","Paid","Not Paid")
        val formatValues = arrayOf("Select","PDF")
        val YearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, yearValues)
        val MonthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, monthValues)
        val StatusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusValues)
        val FormatAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, formatValues)
        yearID.adapter = YearAdapter
        MonthID.adapter = MonthAdapter
        statusID.adapter = StatusAdapter
        formatID.adapter = FormatAdapter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Cusname = listOf("John Doe", "Jane Smith", "Aby Jose", "Anna Rose", "John Doe", "John Doe")
        val crfno = listOf("000100", "000105", "000110", "000102", "000100", "000100")
        val custAvatar = listOf(R.drawable.avatar3, R.drawable.avatar, R.drawable.avatar4, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar3)
        val status = listOf("PAID", "PENDING", "PAID", "PAID", "PENDING", "PENDING")
        val adapter = reportsAdapter(Cusname, custAvatar,crfno,status)
        binding.reportsView.layoutManager = LinearLayoutManager(requireContext())
        binding.reportsView.adapter = adapter
    }

    companion object {

    }
}