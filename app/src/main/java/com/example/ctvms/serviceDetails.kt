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
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import customAlert
import java.time.LocalDate
import java.time.ZoneId


class serviceDetails : Fragment() {
    private var buttonClickListener: OnAddButtonClickListener? = null
    private lateinit var cusBasePlan: AppCompatSpinner
    private lateinit var cusChipID: TextInputEditText
    private lateinit var installationDate: TextInputEditText
    private lateinit var planStartDate: TextInputEditText
    private lateinit var AddOnPackage1: AppCompatSpinner
    private lateinit var AddOnPackage2: AppCompatSpinner
    private lateinit var AddOnPackage3: AppCompatSpinner
    private lateinit var AddOnPackage4: AppCompatSpinner

    interface OnAddButtonClickListener {
        fun onAddButtonClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_details, container, false)
        cusBasePlan = view.findViewById(R.id.BasePlanValue)
        cusChipID = view.findViewById(R.id.cusChipIDValue)
        installationDate = view.findViewById(R.id.InstallationValue)
        planStartDate = view.findViewById(R.id.PlanStartValue)
        AddOnPackage1 = view.findViewById(R.id.addonValue1)
        AddOnPackage2 = view.findViewById(R.id.addonValue2)
        AddOnPackage3 = view.findViewById(R.id.addonValue3)
        AddOnPackage4 = view.findViewById(R.id.addonValue4)
        val BasePlans = arrayOf(
            "Select",
            "CLASSIC MEDIUM @ Rs. 130/-",
            "CLASSIC PRIME @ Rs. 232.95/-",
            "DIGITAL PREMIUM HD @ Rs. 300.35/-",
            "KERALA VISION HD ULTIMATE @ Rs. 585.50/-"
        )
        val AddOnPlans = arrayOf(
            "Select",
            "FLOWERS @ Rs. 0/-",
            "STAR VALUE PACK MALAYALAM @ Rs. 54/-",
            "ZEE PRIME PACK MALAYALAM SD @ Rs. 10/-"
        )
        val fontPath = R.font.poppins
        val CusBasePlanAdapter = CustomSpinnerAdapterBlack(
            requireContext(),
            BasePlans, fontPath
        )
        val AddOnPlanAdapter = CustomSpinnerAdapterBlack(
            requireContext(),
            AddOnPlans, fontPath
        )

        // Set adapters to spinners
        cusBasePlan.adapter = CusBasePlanAdapter
        AddOnPackage1.adapter = AddOnPlanAdapter
        AddOnPackage2.adapter = AddOnPlanAdapter
        AddOnPackage3.adapter = AddOnPlanAdapter
        AddOnPackage4.adapter = AddOnPlanAdapter
        val installation = view.findViewById<ImageView>(R.id.InstallationIcon)
        val planStart = view.findViewById<ImageView>(R.id.planstartIcon)// Assuming it's an ImageView
        installation.setOnClickListener {
            val today = LocalDate.now()
            val maxDate = today.plusDays(3)
            val minDate = today.minusDays(3)
            val currentYear = today.year
            val currentMonth = today.monthValue - 1
            val currentDay = today.dayOfMonth

            val datePickerDialog = DatePickerDialog(
                requireContext(), R.style.UserDialog, // Add your theme resource ID here
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    installationDate.setText(selectedDate)
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
        planStart.setOnClickListener {
            val today = LocalDate.now()
            val maxDate = today.plusDays(3)
            val minDate = today.minusDays(3)
            val currentYear = today.year
            val currentMonth = today.monthValue - 1
            val currentDay = today.dayOfMonth

            val datePickerDialog = DatePickerDialog(
                requireContext(), R.style.UserDialog, // Add your theme resource ID here
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    planStartDate.setText(selectedDate)
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myButton = view.findViewById<Button>(R.id.addCusBtn)
        myButton.setOnClickListener {
            // Notify the activity when the button is clicked
            buttonClickListener?.onAddButtonClicked()
        }
    }

    fun setOnAddButtonClickListener(listener: OnAddButtonClickListener) {
        this.buttonClickListener = listener
    }

    data class cusServiceData(
        val basePlan: String,
        val chipID: String,
        val installationDate: String,
        val planStartDate: String,
        val addOn1: String,
        val addOn2: String,
        val addOn3: String,
        val addOn4: String
    )

    fun getServiceData(): cusServiceData? {
        val basePlanValue = cusBasePlan.selectedItem.toString()
        val chipIDValue = cusChipID.text.toString()
        val installationDateValue = installationDate.text.toString()
        val planStartDateValue = planStartDate.text.toString()
        val addOn1Value = AddOnPackage1.selectedItem.toString()
        val addOn2Value = AddOnPackage2.selectedItem.toString()
        val addOn3Value = AddOnPackage3.selectedItem.toString()
        val addOn4Value = AddOnPackage4.selectedItem.toString()
        if (basePlanValue == "Select") {
            showCustomAlert("Please Select Base Plan")
            return null
        }
        if (chipIDValue.isEmpty()) {
            showCustomAlert("Please Enter Chip ID")
            return null
        }
        if (installationDateValue.isEmpty()) {
            showCustomAlert("Please Choose Installation Date")
            return null
        }
        if (planStartDateValue.isEmpty()) {
            showCustomAlert("Please Choose Plan Start Date")
            return null
        }
        return cusServiceData(
            basePlanValue,
            chipIDValue,
            installationDateValue,
            planStartDateValue,
            addOn1Value,
            addOn2Value,
            addOn3Value,
            addOn4Value
        )
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