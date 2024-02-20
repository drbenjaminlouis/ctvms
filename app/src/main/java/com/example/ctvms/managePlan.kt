package com.example.ctvms

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class managePlan : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_plan)
        val add_plan = findViewById<LinearLayout>(R.id.addPlanLayout)
        val edit_plan = findViewById<LinearLayout>(R.id.editPlanLayout)
        add_plan.setOnClickListener(){
            val intent = Intent(this, addPlan::class.java)
            startActivity(intent)
        }
        edit_plan.setOnClickListener(){
            val intent = Intent(this, editPlan::class.java)
            startActivity(intent)
        }
    }
}