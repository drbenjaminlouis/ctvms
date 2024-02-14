package com.example.ctvms.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ctvms.R
import com.example.ctvms.adapter.CustomerData
import com.example.ctvms.databinding.FragmentTransactionsBinding
import com.example.ctvms.adapter.TransactionsmainAdapter


class transactionsFragment : Fragment() {
    private lateinit var binding: FragmentTransactionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateVales = listOf<String>("01 Jan 2024","02 Jan 2024","03 Jan 2024","04 Jan 2024")
        val customerDataList = listOf(
            listOf(
                CustomerData("John", "CRF123", R.drawable.avatar, "$100"),
                CustomerData("Alice", "CRF456", R.drawable.avatar2, "$200")
            ),
            listOf(
                CustomerData("Bob", "CRF789", R.drawable.avatar3, "$150"),
                CustomerData("Eve", "CRF012", R.drawable.avatar2, "$250")
            ),
            listOf(
                CustomerData("Aby", "CRF789", R.drawable.avatar4, "$150"),
                CustomerData("Anna", "CRF012", R.drawable.avatar2, "$250")
            ),
            listOf(
                CustomerData("John Doe", "CRF123", R.drawable.avatar, "$100"),
                CustomerData("Jane Smith", "CRF456", R.drawable.avatar2, "$200")
            ),
        )
        val adapter = TransactionsmainAdapter(dateVales,customerDataList)
        binding.maintransRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.maintransRecycler.adapter = adapter
    }

    companion object {

    }
}