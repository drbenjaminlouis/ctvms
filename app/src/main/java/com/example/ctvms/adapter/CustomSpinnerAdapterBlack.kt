
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapterBlack(context: Context, private val items: Array<String>, private val fontPath: Int) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.setTextColor(Color.WHITE) // Change text color of the selected item
        view.textSize = 13F
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.setTextColor(Color.WHITE) // Change text color of the dropdown items
        return view
    }
    private fun applyCustomFont(textView: TextView) {
        val typeface = Typeface.createFromAsset(context.assets, fontPath.toString())
        textView.typeface = typeface
    }
}
