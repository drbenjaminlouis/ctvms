package com.example.ctvms

import CustomerDataAdapter
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class CustomerSearchContainer : Fragment() {
    private var onCrfValueClickListener: OnCrfValueClickListener? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomerDataAdapter
    private lateinit var sharedPreferences: SharedPreferences
    interface OnCrfValueClickListener {
        fun onCrfValueClicked(crfValue: String,custName:String)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCrfValueClickListener) {
            onCrfValueClickListener = context
        } else {
            throw RuntimeException("$context must implement OnCrfValueClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_customer_search_container, container, false)
        recyclerView = rootView.findViewById(R.id.paymentCustomerListView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sharedPreferences = requireActivity().getSharedPreferences("customer_data", Context.MODE_PRIVATE)

        // Initialize adapter with an empty list initially
        adapter = CustomerDataAdapter(emptyList()) { crfValue,custName ->
            // Call the interface method when crfValue is clicked
            onCrfValueClickListener?.onCrfValueClicked(crfValue,custName)
        }
        recyclerView.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromFirestore()

        return rootView
    }



    private fun fetchDataFromFirestore() {
        // Replace "a@gmail.com" with the actual admin email
        val adminEmail = "abyjose377@gmail.com"
        val db = Firebase.firestore

        db.collection("admins")
            .document(adminEmail)
            .collection("customers")
            .get()
            .addOnSuccessListener { result ->
                val customerList = mutableListOf<Customer>()

                for ((index, document) in result.documents.withIndex()) {
                    if (index >= 10) break // Stop after first 10 data

                    val customerName = document.getString("cusName")
                    val crfNo = document.getString("cusCRF")
                    val gender = document.getString("cusGender")

                    val avatar = when (gender) {
                        "Male" -> getRandomMaleAvatar()
                        "Female" -> getRandomFemaleAvatar()
                        else -> R.drawable.avatar
                    }

                    customerName?.let { name ->
                        crfNo?.let { crf ->
                            customerList.add(Customer(name, crf, avatar))
                        }
                    }
                }

                // Update adapter data
                adapter.updateData(customerList)

                // Save data to SharedPreferences
                saveDataToSharedPreferences(customerList)
            }
            .addOnFailureListener { exception ->
                println("Error fetching data from Firestore: $exception")
            }
    }

    private fun saveDataToSharedPreferences(customerList: List<Customer>) {
        val editor = sharedPreferences.edit()
        for ((index, customer) in customerList.withIndex()) {
            editor.putString("Name$index", customer.name)
            editor.putString("CrfNo$index", customer.crfNo)
            editor.putInt("Avatar$index", customer.avatarResourceId)
        }
        editor.apply()
    }

    private fun getRandomMaleAvatar(): Int {
        val maleAvatars = listOf(
            R.drawable.avatar,
            R.drawable.avatar3,
            R.drawable.avatar4
        )
        return maleAvatars.random()
    }

    private fun getRandomFemaleAvatar(): Int {
        val femaleAvatars = listOf(
            R.drawable.avatar2,
            R.drawable.female_avatar2,
            R.drawable.female_avatar4
        )
        return femaleAvatars.random()
    }
}