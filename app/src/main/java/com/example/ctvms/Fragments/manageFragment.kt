package com.example.ctvms.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ctvms.Customer
import com.example.ctvms.R
import com.example.ctvms.adapter.manageAdapter
import com.example.ctvms.addCustomer
import com.example.ctvms.databinding.FragmentManageBinding
import com.example.ctvms.dueChecker
import com.example.ctvms.managePlan
import com.google.firebase.firestore.FirebaseFirestore

class manageFragment : Fragment() {
    private lateinit var binding: FragmentManageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: manageAdapter // Declare adapter as a property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageBinding.inflate(inflater, container, false)
        val add_user = binding.newuserlayout
        val due_checker = binding.duecheckerlayout
        val plan_manager = binding.planslayout
        add_user.setOnClickListener() {
            val intent = Intent(requireContext(), addCustomer::class.java)
            startActivity(intent)
        }
        due_checker.setOnClickListener(){
            val intent = Intent(requireContext(), dueChecker::class.java)
            startActivity(intent)
        }
        plan_manager.setOnClickListener(){
            val intent = Intent(requireContext(), managePlan::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("CustomerData", Context.MODE_PRIVATE)

        // Initialize RecyclerView adapter
        adapter = manageAdapter(emptyList()) // Pass empty list initially
        binding.managerecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.managerecyclerview.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        val adminEmail = "abyjose377@gmail.com" // Replace this with the actual admin email
        db.collection("admins")
            .document(adminEmail)
            .collection("customers")
            .get()
            .addOnSuccessListener { result ->
                val customerList = mutableListOf<Customer>()

                for ((index, document) in result.withIndex()) {
                    if (index >= 10) break // Stop after first 10 data

                    val customerName = document.getString("cusName")
                    val crfNo = document.getString("cusCRF")
                    val gender = document.getString("cusGender")

                    // Select random avatar based on gender
                    val avatar = when (gender) {
                        "Male" -> getRandomMaleAvatar()
                        "Female" -> getRandomFemaleAvatar()
                        else -> R.drawable.avatar // Default avatar if gender is not specified or invalid
                    }

                    customerName?.let { name ->
                        crfNo?.let { crf ->
                            customerList.add(Customer(name, crf, avatar))
                        }
                    }
                }

                // Save first 10 data to SharedPreferences
                saveDataToSharedPreferences(customerList)

                // Update adapter data
                adapter.updateData(customerList)
            }
            .addOnFailureListener { exception ->
                println("Error fetching data from Firestore: $exception")
            }
    }

    // Function to select a random male avatar
    private fun getRandomMaleAvatar(): Int {
        val maleAvatars = listOf(
            R.drawable.avatar,
            R.drawable.avatar3,
            R.drawable.avatar4
            // Add more male avatar resources as needed
        )
        return maleAvatars.random()
    }

    // Function to select a random female avatar
    private fun getRandomFemaleAvatar(): Int {
        val femaleAvatars = listOf(
            R.drawable.avatar2,
            R.drawable.female_avatar2,
            R.drawable.female_avatar4
            // Add more female avatar resources as needed
        )
        return femaleAvatars.random()
    }

    // Function to save first 10 data to SharedPreferences
    private fun saveDataToSharedPreferences(customerList: List<Customer>) {
        val editor = sharedPreferences.edit()
        for ((index, customer) in customerList.withIndex()) {
            editor.putString("Name$index", customer.name)
            editor.putString("CrfNo$index", customer.crfNo)
            editor.putInt("Avatar$index", customer.avatarResourceId)
        }
        editor.apply()
    }
}
