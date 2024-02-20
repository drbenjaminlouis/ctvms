package com.example.ctvms

import CustomSpinnerAdapterBlack
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import customAlert
import java.time.LocalDate
import java.time.ZoneId

class customerPersonalInfo : Fragment() {
    private var buttonClickListener: OnButtonClickListener? = null
    private lateinit var cusNameValue: TextInputEditText
    private lateinit var cusEmailValue: TextInputEditText
    private lateinit var cusMobileValue: TextInputEditText
    private lateinit var cusGenderValue: AppCompatSpinner
    private lateinit var cusDOBValue: TextInputEditText
    private lateinit var cusAddressValue: TextInputEditText
    private lateinit var cusCityValue: TextInputEditText
    private lateinit var cusDistrictValue: AppCompatSpinner
    private lateinit var cusStateValue: AppCompatSpinner
    interface OnButtonClickListener {
        fun onButtonClicked()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_customer_personal_info,container,false)
        val dateBtn = view.findViewById<ImageView>(R.id.dobdropicon) // Assuming it's an ImageView
        dateBtn.setOnClickListener {
            val today = LocalDate.now()
            val maxDate = today.minusYears(18) // Subtract 18 years
            val minDate = today.minusYears(70)
            val currentYear = today.year
            val currentMonth = today.monthValue - 1
            val currentDay = today.dayOfMonth

            val datePickerDialog = DatePickerDialog(
                requireContext(), R.style.UserDialog, // Add your theme resource ID here
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    val dateText = view.findViewById<TextInputEditText>(R.id.dob)
                    dateText.setText(selectedDate)
                },
                currentYear,
                currentMonth,
                currentDay
            )

            // Set the maximum date to 18 years before today (in milliseconds)
            datePickerDialog.datePicker.maxDate =
                maxDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            datePickerDialog.datePicker.minDate =
                minDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            datePickerDialog.show()
        }
        val genderID = view.findViewById<Spinner>(R.id.gender)
        val stateID = view.findViewById<Spinner>(R.id.stateValue)
        val districtID = view.findViewById<Spinner>(R.id.districtValue)
        val genderValues = arrayOf("Select", "Male", "Female", "Others")
        val stateValues = arrayOf("Select","Kerala", "Karnataka")
        val districtValues = arrayOf("Select","Kottayam", "Ernakulam")
        val fontPath = R.font.poppins
        val genderAdapter = CustomSpinnerAdapterBlack(
            requireContext(),
            genderValues,fontPath
        )
        val stateAdapter = CustomSpinnerAdapterBlack(
            requireContext(),
            stateValues,fontPath
        )
        val districtAdapter = CustomSpinnerAdapterBlack(
            requireContext(),
            districtValues,fontPath
        )

        // Set adapters to spinners
        genderID.adapter = genderAdapter
        stateID.adapter = stateAdapter
        districtID.adapter = districtAdapter
        return view
    }
    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.buttonClickListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myButton = view.findViewById<Button>(R.id.cuscontinuebtn)
        cusNameValue = view.findViewById(R.id.cusNameValue)
        cusEmailValue = view.findViewById(R.id.cusemailValue)
        cusMobileValue = view.findViewById(R.id.cusMobileValue)
        cusGenderValue = view.findViewById(R.id.gender)
        cusDOBValue = view.findViewById(R.id.dob)
        cusAddressValue = view.findViewById(R.id.cusAddressValue)
        cusCityValue = view.findViewById(R.id.cusCityValue)
        cusDistrictValue = view.findViewById(R.id.districtValue)
        cusStateValue = view.findViewById(R.id.stateValue)

        myButton.setOnClickListener {
            // Notify the activity when the button is clicked
            buttonClickListener?.onButtonClicked()
        }
    }

    data class cusPersonal(val cusName: String,val cusEmail:String,val cusMobile:String,val cusGender:String,val cusDOB:String,val cusAddress:String,val cusCity:String,val cusDistrict:String,val cusState:String)
    fun getCusData(): cusPersonal?{
        val cusName = cusNameValue.text.toString()
        val cusEmail = cusEmailValue.text.toString()
        val cusMobile = cusMobileValue.text.toString()
        val cusGender = cusGenderValue.selectedItem.toString()
        val cusDOB = cusDOBValue.text.toString()
        val cusAddress = cusAddressValue.text.toString()
        val cusCity = cusCityValue.text.toString()
        val cusDistrict = cusDistrictValue.selectedItem.toString()
        val cusState = cusStateValue.selectedItem.toString()
        if (cusName.isEmpty()){
            showCustomAlert("Please Enter Customer Name")
            return null
        }
        if (cusEmail.isEmpty()){
            showCustomAlert("Please Enter Customer Email")
            return null
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cusEmail).matches()) {
            showCustomAlert("Enter a valid Email")
            return null
        }
        if (cusMobile.isEmpty()){
            showCustomAlert("Please Enter Customer Mobile Number")
            return null
        }else if (!android.util.Patterns.PHONE.matcher(cusMobile).matches()|| cusMobile.length<10) {
            showCustomAlert("Enter a valid Mobile Number")
            return null
        }
        if (cusGender.isEmpty()||cusGender == "Select"){
            showCustomAlert("Please Select Customer Gender")
            return null
        }
        if (cusDOB.isEmpty()){
            showCustomAlert("Please Select Customer DOB")
            return null
        }
        if (cusAddress.isEmpty()){
            showCustomAlert("Please Enter Customer Address")
            return null
        }
        if (cusCity.isEmpty()){
            showCustomAlert("Please Enter Customer City")
            return null
        }
        if (cusDistrict.isEmpty()||cusDistrict == "Select"){
            showCustomAlert("Please Select Customer District")
            return null
        }
        if (cusState.isEmpty()||cusState == "Select"){
            showCustomAlert("Please Select Customer State")
            return null
        }
        return cusPersonal(cusName,cusEmail,cusMobile,cusGender,cusDOB,cusAddress,cusCity,cusDistrict,cusState)
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
}