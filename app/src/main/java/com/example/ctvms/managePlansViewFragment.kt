package com.example.ctvms

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.adapter.managePlansAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import customAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class managePlansViewFragment : Fragment() {
    private lateinit var adapter: managePlansAdapter
    private val SHARED_PREF_NAME = "plan_data"
    private val PLAN_LIST_KEY = "plan_list"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manage_plans_view, container, false)
        val manageplancontainer = view.findViewById<RecyclerView>(R.id.plansFragContainer)
        adapter = managePlansAdapter(emptyList())
        // Use LinearLayoutManager for RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        manageplancontainer.layoutManager = layoutManager
        manageplancontainer.adapter = adapter
        fetchPlansDataFromFirestore()
        return view.rootView
    }
    private fun fetchPlansDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val plansList = mutableListOf<Plans>()
                val collectionReference = db.collection("/admins/abyjose377@gmail.com/Plans")
                val querySnapshot = collectionReference.get().await()

                for (document in querySnapshot.documents) {
                    val planName = document.getString("planName") ?: ""
                    val fullPlanID = document.getString("planID") ?: ""
                    val lastThreeDigits = fullPlanID.takeLast(3)
                    val formattedPlanID = "#$lastThreeDigits"
                    val channelCount = document.getString("channelCount") ?: ""
                    val planPrice = document.getString("price") ?: ""
                    val formattedPlanPrice = "$planPrice/-"
                    val plan = Plans(planName, formattedPlanID, channelCount,R.drawable.plan_bg,formattedPlanPrice,fullPlanID,"","")
                    plansList.add(plan)
                }

                // Update UI on the main thread
                launch(Dispatchers.Main) {
                    adapter.setData(plansList)
                    savePlansToSharedPreferences(requireContext(), plansList)
                }
            } catch (e: Exception) {
                // Handle exceptions
                println("Error fetching plans data: ${e.message}")
                // Show error message to the user
                showCustomAlert("Error", "Failed to fetch plans data", R.drawable.errorinfo)
            }
        }
    }
    private fun savePlansToSharedPreferences(context: Context, plansList: List<Plans>) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(plansList)
        editor.putString(PLAN_LIST_KEY, json)
        editor.apply()
    }
    private fun showCustomAlert(alertTitle: String, alertMessage: String, alertIcon: Int) {
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData(alertTitle, alertIcon, alertMessage)
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(requireContext())
            .setView(customAlertView)
            .create()
        val okBtn: Button = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            alert.dismiss()
        }
    }
}