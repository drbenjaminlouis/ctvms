package com.example.ctvms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class AdminCPassDetails : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_c_pass_details, container, false)
        val continueBtn = view.findViewById<Button>(R.id.cpcontinueBtn)

        continueBtn.setOnClickListener {
            val otpfrag = AdminCPOTP()
            val fillIcon = requireActivity().findViewById<ImageView>(R.id.fillIcon)
            val fillLine = requireActivity().findViewById<ImageView>(R.id.fillLine)
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                // Replace the contents of the FragmentContainerView with your fragment
                replace(R.id.adminCPView, otpfrag)
                addToBackStack(null)
                commit()
                fillIcon.setImageResource(R.drawable.success)
                fillLine.setImageResource(R.drawable.greenline)
            }
        }
        return view
    }
}
