import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.ctvms.R

class customAlert(
    private val context: Context
) {
    private val view: View = LayoutInflater.from(context).inflate(R.layout.customalert, null)
    private val head: TextView = view.findViewById(R.id.alertHead)
    private val image: ImageView = view.findViewById(R.id.alertIcon)
    private val messageID: TextView = view.findViewById(R.id.alertMessage)
    private val okBtn: Button = view.findViewById(R.id.okBtn)

    // Function to set custom data
    fun setData(title: String, iconResId: Int, message: String) {
        head.text = title
        image.setImageResource(iconResId)
        messageID.text = message
    }

    // Function to get the custom alert view
    fun getView(): View {
        return view
    }

    fun getOkButton(): Button {
        return view.findViewById(R.id.okBtn)
    }
}
