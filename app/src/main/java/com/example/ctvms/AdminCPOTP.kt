package com.example.ctvms

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import customAlert


class AdminCPOTP : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_c_p_o_t_p, container, false)
        val changebtn = view.findViewById<Button>(R.id.cpchangeBtn)
        changebtn.setOnClickListener(){
            customDialog()
        }
        return view
    }

    private fun customDialog() {
        val dialog = Dialog(requireContext())
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData("Success", R.drawable.success, "Password Updated Successfully")
        val customAlertView = myCustomAlert.getView()
        customAlertView.findViewById<Button>(R.id.okBtn)
        val alert = AlertDialog.Builder(context)
            .setView(customAlertView)
            .create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.customdialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val verificationIcon = requireActivity().findViewById<ImageView>(R.id.verificationIcon)
        val verificationLine = requireActivity().findViewById<ImageView>(R.id.veificationLine)
        val yesBtn: Button = dialog.findViewById(R.id.yesBtn)
        val noBtn: Button = dialog.findViewById(R.id.noBtn)
        val okBtn: Button = myCustomAlert.getOkButton()

        yesBtn.setOnClickListener(){
            dialog.dismiss()
            alert.show()
            val confirmIcon = requireActivity().findViewById<ImageView>(R.id.confirmIcon)
            confirmIcon.setImageResource(R.drawable.success)
            okBtn.setOnClickListener(){
                alert.dismiss()
                val intent = Intent(requireContext(), adminHome::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
        }

        noBtn.setOnClickListener(){
            dialog.dismiss()
        }
        dialog.show()
        verificationIcon.setImageResource(R.drawable.success)
        verificationLine.setImageResource(R.drawable.greenline)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            requireActivity().supportFragmentManager.popBackStack()
            val fillIcon = requireActivity().findViewById<ImageView>(R.id.fillIcon)
            val fillLine = requireActivity().findViewById<ImageView>(R.id.fillLine)
            fillIcon.setImageResource(R.drawable.notcompleted)
            fillLine.setImageResource(R.drawable.line)
        }
    }

    companion object {

    }
}