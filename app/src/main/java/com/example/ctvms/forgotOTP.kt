package com.example.ctvms

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import customAlert

class forgotOTP : Fragment() {
    private var buttonClickListener: OnVerifyButtonClickListener? = null
    private lateinit var Value1: TextInputEditText
    private lateinit var Value2: TextInputEditText
    private lateinit var Value3: TextInputEditText
    private lateinit var Value4: TextInputEditText
    private lateinit var Value5: TextInputEditText
    private lateinit var Value6: TextInputEditText
    interface OnVerifyButtonClickListener {
        fun onVerifyButtonClicked()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_o_t_p, container, false)
        // Inflate the layout for this fragment
        val verifyBtn = view.findViewById<Button>(R.id.forgotOTPVerifyBtn)
        verifyBtn.setOnClickListener {
            buttonClickListener?.onVerifyButtonClicked()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Value1 = view.findViewById(R.id.fotpVal1)
        Value2 = view.findViewById(R.id.fotpVal2)
        Value3 = view.findViewById(R.id.fotpVal3)
        Value4 = view.findViewById(R.id.fotpVal4)
        Value5 = view.findViewById(R.id.fotpVal5)
        Value6 = view.findViewById(R.id.fotpVal6)
        setupTextChangeListeners()
    }
    private fun setupTextChangeListeners() {
        setListener(Value1, Value2, null)
        setListener(Value2, Value3, Value1)
        setListener(Value3, Value4, Value2)
        setListener(Value4, Value5, Value3)
        setListener(Value5, Value6, Value4)
        setListener(Value6, null, Value5) // Last field, no next field
    }

    private fun setListener(editText: TextInputEditText, nextEditText: TextInputEditText?, prevEditText: TextInputEditText?) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    if (nextEditText != null && nextEditText.text?.isEmpty()==true) {
                        nextEditText.requestFocus()
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (editText.text?.isEmpty()==true) {
                    editText.setText("")
                    prevEditText?.setText("")
                    // Clear the text of the previous field
                    prevEditText?.requestFocus()
                    return@OnKeyListener true
                }
            }
            false
        })
    }
    data class FOTPData(val Value1:String,val Value2:String,val Value3:String,val Value4:String,val Value5:String,val Value6:String)

    fun getFOTPData(): FOTPData? {
        val Value1Text = Value1.text.toString()
        val Value2Text = Value2.text.toString()
        val Value3Text = Value3.text.toString()
        val Value4Text = Value4.text.toString()
        val Value5Text = Value5.text.toString()
        val Value6Text = Value6.text.toString()
        if (Value1Text.isEmpty()){
            showCustomAlert("Please Enter OTP")
            return null
        }
        if (Value2Text.isEmpty()){
            showCustomAlert("Please Enter OTP")
            return null
        }
        if (Value3Text.isEmpty()){
            showCustomAlert("Please Enter OTP")
            return null
        }
        if (Value4Text.isEmpty()){
            showCustomAlert("Please Enter OTP")
            return null
        }
        if (Value5Text.isEmpty()){
            showCustomAlert("Please Enter OTP")
            return null
        }
        if (Value6Text.isEmpty()){
            showCustomAlert("Please Enter OTP")
            return null
        }
        return FOTPData(Value1Text, Value2Text, Value3Text, Value4Text, Value5Text, Value6Text)
    }
    fun showCustomAlert(errorMessage: String) {
        val myCustomAlert = customAlert(requireContext())
        myCustomAlert.setData("Please Fill In All Details", R.drawable.errorinfo, errorMessage)
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(context)
            .setView(customAlertView)
            .create()
        val okBtn: Button = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            alert.dismiss()
        }
    }
    fun setOnVerifyButtonClickListener(listener: OnVerifyButtonClickListener) {
        this.buttonClickListener = listener
    }
}
