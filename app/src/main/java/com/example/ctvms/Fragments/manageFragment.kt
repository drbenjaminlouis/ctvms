package com.example.ctvms.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ctvms.R
import com.example.ctvms.adapter.manageAdapter
import com.example.ctvms.adapter.recentAdapter
import com.example.ctvms.databinding.FragmentHomeBinding
import com.example.ctvms.databinding.FragmentManageBinding


class manageFragment : Fragment() {
    private lateinit var binding: FragmentManageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Cusname = listOf("John Doe", "Jane Smith", "Aby Jose", "Anna Rose", "John Doe", "John Doe")
        val Cuscrfno = listOf("000100", "000105", "000110", "000102", "000100", "000100")
        val custAvatar = listOf(R.drawable.avatar3, R.drawable.avatar, R.drawable.avatar4, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar3)
        val adapter = manageAdapter(Cusname, Cuscrfno, custAvatar)
        binding.managerecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.managerecyclerview.adapter = adapter
    }

    companion object {

    }
}