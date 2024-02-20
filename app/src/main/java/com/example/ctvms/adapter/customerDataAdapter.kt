import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctvms.Customer
import com.example.ctvms.databinding.FragmentPaymentCustomerViewBinding

class CustomerDataAdapter(private var customers: List<Customer>,private val onItemClick: (String,String) -> Unit): RecyclerView.Adapter<CustomerDataAdapter.CustomerDataViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerDataAdapter.CustomerDataViewHolder {
        val binding = FragmentPaymentCustomerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: CustomerDataViewHolder, position: Int) {
        val customer = customers[position]
        holder.bind(customer)
        holder.itemView.setOnClickListener {
            onItemClick(customer.crfNo,customer.name) // Pass the CRF value when an item is clicked
        }
    }

    inner class CustomerDataViewHolder(private val binding: FragmentPaymentCustomerViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: Customer) {
            binding.paymentcustName.text = customer.name
            binding.crfValue.text = customer.crfNo
            binding.paymentcustAvatar.setImageResource(customer.avatarResourceId)
        }
    }
    fun updateData(newCustomers: List<Customer>) {
        customers = newCustomers
        notifyDataSetChanged() // Notify RecyclerView that the data has changed
    }
}
