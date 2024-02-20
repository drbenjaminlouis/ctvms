package com.example.ctvms

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class dueChecker : AppCompatActivity(), CustomerSearchContainer.OnCrfValueClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_due_checker)
        val backicon = findViewById<ImageView>(R.id.backBtn)
        backicon.setOnClickListener(){
            onBackPressed()
        }
    }

    override fun onCrfValueClicked(crfValue: String, custName: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val dueDetailsFrag = dueCheckerDetais().apply {
            arguments = Bundle().apply {
                putString("crfValue", crfValue)
                putString("cusName",custName)
            }
        }
        fragmentTransaction.replace(R.id.duefragment, dueDetailsFrag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.duefragment)

        // Handle back button press based on the current fragment
        if (currentFragment is CustomerSearchContainer) { // Replace YourInitialFragment with the name of your initial fragment
            // Handle back button press when the initial fragment is shown
            super.onBackPressed()
        } else {
            fragmentManager.popBackStack()
        }
    }
}