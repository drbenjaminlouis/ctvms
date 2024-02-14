
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.example.ctvms.R
import com.example.ctvms.setPassword
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.ZoneId

class AdminPersonalInfoFragment : Fragment() {

    private var buttonClickListener: OnButtonClickListener? = null
    private lateinit var adminName: TextInputEditText
    private lateinit var adminEmail: TextInputEditText
    private lateinit var adminMobile: TextInputEditText
    private lateinit var adminGender: AppCompatSpinner
    private lateinit var adminDOB: TextInputEditText
    private lateinit var adminAddress: TextInputEditText
    private lateinit var adminDistrict: AppCompatSpinner
    private lateinit var adminState: AppCompatSpinner
    interface OnButtonClickListener {
        fun onButtonClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_personal_info, container, false)
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


        // Access the spinners from the inflated layout
        val genderID = view.findViewById<Spinner>(R.id.gender)
        val stateID = view.findViewById<Spinner>(R.id.state)
        val districtID = view.findViewById<Spinner>(R.id.district)
        val Conbutton = view.findViewById<Button>(R.id.continuebtn)

        // Create adapter data
        val genderValues = arrayOf("Select", "Male", "Female", "Others")
        val stateValues = arrayOf("Select","Kerala", "Karnataka")
        val districtValues = arrayOf("Select","Kottayam", "Ernakulam")
        val fontPath = R.font.poppins

        // Create adapters using requireContext()
        val genderAdapter = CustomSpinnerAdapter(
            requireContext(),
            genderValues,fontPath
        )
        val stateAdapter = CustomSpinnerAdapter(
            requireContext(),
            stateValues,fontPath
        )
        val districtAdapter = CustomSpinnerAdapter(
            requireContext(),
            districtValues,fontPath
        )

        // Set adapters to spinners
        genderID.adapter = genderAdapter
        stateID.adapter = stateAdapter
        districtID.adapter = districtAdapter


        Conbutton.setOnClickListener(){
            val intent = Intent(context, setPassword::class.java)
            startActivity(intent)
        }
        Conbutton.setOnClickListener {
            val title = requireActivity().findViewById<TextView>(R.id.createtitle)
            val peronalChecker = requireActivity().findViewById<ImageView>(R.id.personalChecker)
            val PersonalLine = requireActivity().findViewById<ImageView>(R.id.personalLine)
            title.text = "Set Password"
            peronalChecker.setImageResource(R.drawable.success)
            PersonalLine.setImageResource(R.drawable.greenline)

            var fr = getFragmentManager()?.beginTransaction()
            fr?.add(R.id.fragContainer, setPassword())
            fr?.addToBackStack(null)  // Enabling back navigation
            fr?.commit()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myButton = view.findViewById<Button>(R.id.continuebtn)
        adminName = view.findViewById<TextInputEditText>(R.id.fullname)
        adminEmail = view.findViewById<TextInputEditText>(R.id.email)
        adminMobile = view.findViewById<TextInputEditText>(R.id.mobile)
        adminGender = view.findViewById<AppCompatSpinner>(R.id.gender)
        adminDOB = view.findViewById<TextInputEditText>(R.id.dob)
        adminAddress = view.findViewById<TextInputEditText>(R.id.address)
        adminState = view.findViewById<AppCompatSpinner>(R.id.state)
        adminDistrict = view.findViewById<AppCompatSpinner>(R.id.district)

        // Set click listener
        myButton.setOnClickListener {
            // Notify the activity when the button is clicked
            buttonClickListener?.onButtonClicked()
        }
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.buttonClickListener = listener
    }
    private fun showError(textBox: TextInputEditText, s: String) {
        textBox.error = s
    }
    data class AdminPesonalData(val adminName: String, val adminEmail: String, val adminMobile: String,val adminGender: String,val adminDOB: String,val adminAddress: String,val adminDistrict: String, val adminState: String)
    // Method to retrieve the admin name entered in the TextInputEditText
    fun getAdminPersonalData(): AdminPesonalData? {
        val adminNameText = adminName.text.toString()
        if (adminNameText.isEmpty()) {
            showCustomAlert("Enter Full Name")
            return null
        }

        val adminEmailText = adminEmail.text.toString()
        if (adminEmailText.isEmpty()) {
            showCustomAlert("Enter Email")
            return null
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(adminEmailText).matches()) {
            showCustomAlert("Enter a valid Email")
            return null
        }

        val adminMobileText = adminMobile.text.toString()
        if (adminMobileText.isEmpty()) {
            showCustomAlert("Enter Mobile Number")
            return null
        } else if (!android.util.Patterns.PHONE.matcher(adminMobileText).matches()|| adminMobileText.length<10) {
            showCustomAlert("Enter a valid Mobile Number")
            return null
        }

        val adminDOBText = adminDOB.text.toString()
        if (adminDOBText.isEmpty()) {
            showCustomAlert("Select Date of Birth")
            return null
        }

        val adminAddressText = adminAddress.text.toString()
        if (adminAddressText.isEmpty()) {
            showCustomAlert("Enter Address")
            return null
        }

        // Get the values of the spinner fields
        val adminGender = adminGender.selectedItem.toString()
        val adminDistrict = adminDistrict.selectedItem.toString()
        val adminState = adminState.selectedItem.toString()
        if (adminGender == "Select"){
            showCustomAlert("Select Gender")
            return null
        }
        if (adminDistrict == "Select"){
            showCustomAlert("Select District")
            return null
        }
        if (adminState == "Select"){
            showCustomAlert("Select State")
            return null
        }
        // Create and return the Data object
        return AdminPesonalData(adminNameText, adminEmailText, adminMobileText, adminGender, adminDOBText, adminAddressText, adminDistrict, adminState)
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