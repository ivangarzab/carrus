package com.ivangarzab.carbud.ui.overview

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.data.DueDateFormat
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.data.isPastDue
import com.ivangarzab.carbud.databinding.ItemServiceBinding
import com.ivangarzab.carbud.prefs
import com.ivangarzab.carbud.util.extensions.getShortenedDate
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Ivan Garza Bermea.
 */
class ServiceListAdapter(
    private val resources: Resources,
    private val theme: Resources.Theme,
    private val services: List<Service>,
    val onItemClicked: (Service) -> Unit,
    val onEditClicked: (Service) -> Unit,
    val onDeleteClicked: (Service) -> Unit
) : RecyclerView.Adapter<ServiceListAdapter.ServiceListViewHolder>() {

    inner class ServiceListViewHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceListViewHolder {
        ItemServiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).also {
            return ServiceListViewHolder(it)
        }
    }

    override fun getItemCount(): Int = services.size

    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        with(holder) {
            with(services[position]) {
                binding.let { binding ->
                    generateItemServiceState(position, this).let { state ->
                        binding.state = state
                        binding.root.setOnClickListener { onExpandToggle(binding, this) }
                        binding.serviceItemExpandIcon.setOnClickListener { onExpandToggle(binding, this) }
                        binding.serviceItemTrashIcon.setOnClickListener { onDeleteClicked(this) }
                        binding.serviceItemEditIcon.setOnClickListener { onEditClicked(this) }
                    }
                }
            }
        }
    }

    private fun onExpandToggle(binding: ItemServiceBinding, service: Service) {
        binding.state = binding.state?.let {
            it.copy(expanded = it.expanded.not())
        }
        onItemClicked(service)
    }

    private fun generateItemServiceState(position: Int, data: Service): ItemServiceState =
        ItemServiceState(
            position = position,
            name = data.name,
            repairDateFormat = data.repairDate.getShortenedDate(),
            details = resources.getString(R.string.service_details, data.brand, data.type),
            price = resources.getString(R.string.price_money, data.cost),
            dueDateFormat = when (data.isPastDue()) {
                true -> {
                    /*binding.serviceItemContentText.setTextColor(Color.RED)
                    binding.serviceItemContentText.setTypeface(null, Typeface.BOLD)*/
                    resources.getString(R.string.due).uppercase()
                }
                false -> {
                    TypedValue().let { value ->
                        theme.resolveAttribute(android.R.attr.textColor, value, true)
//                        binding.serviceItemContentText.setTextColor(value.data)
                    }
//                    binding.serviceItemContentText.setTypeface(null, Typeface.NORMAL)
                    (data.dueDate.timeInMillis - Calendar.getInstance().timeInMillis).let { timeLeftInMillis ->
                        TimeUnit.MILLISECONDS.toDays(timeLeftInMillis).let { daysLeft ->
                            when (daysLeft) {
                                0L -> resources.getString(R.string.tomorrow)
                                else -> when (prefs.dueDateFormat) {
                                    DueDateFormat.DATE -> data.dueDate.getShortenedDate()
                                    DueDateFormat.WEEKS -> "${String.format("%.1f", daysLeft / MULTIPLIER_DAYS_TO_WEEKS)} weeks"
                                    DueDateFormat.MONTHS -> "${String.format("%.2f", daysLeft / MULTIPLIER_DAYS_TO_MONTHS)} months"
                                    else -> "$daysLeft ${resources.getString(R.string.days).lowercase()}"
                                }
                            }
                        }
                    }
                }
            }
        )

    companion object {
        private const val MULTIPLIER_DAYS_TO_WEEKS: Float = 7.0f
        private const val MULTIPLIER_DAYS_TO_MONTHS: Float = 30.43684f
    }
}