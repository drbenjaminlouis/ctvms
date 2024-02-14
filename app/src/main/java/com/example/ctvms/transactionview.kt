import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ctvms.R
import com.example.ctvms.adapter.CustomerData
import com.example.ctvms.adapter.TransactionsmainAdapter
import com.example.ctvms.databinding.FragmentTransactionviewBinding

class transactionview : Fragment() {
    private lateinit var binding: FragmentTransactionviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionviewBinding.inflate(inflater, container, false)

        // Create a list of customer data
        val customerDataList = listOf(
            CustomerData("John Doe", "000100", R.drawable.avatar3, "+1000"),
            CustomerData("Jane Smith", "000105", R.drawable.avatar, "+500"),

        )

        // Initialize the TransactionsmainAdapter with the date label and customer data list
        val dateLabel = listOf("Date 1", "Date 2", "Date 3") // Replace with your actual date labels
        val adapter = TransactionsmainAdapter(dateLabel, listOf(customerDataList))

        binding.transactionsRecycle.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRecycle.adapter = adapter

        return binding.root
    }
}
