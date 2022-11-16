package com.ivangarzab.carbud.ui.overview

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
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
            it.expanded = false
            return ServiceListViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        with(holder) {
            with(services[position]) {
                binding.let {
                    it.data = this
                    it.serviceItemPrice.text = resources.getString(R.string.price_money, cost.toString())
                    it.serviceItemRepairDate.text = repairDate.getShortenedDate()
                    it.serviceItemDetails.text = resources.getString(R.string.service_details, brand, type)
                    it.serviceItemContentText.text = when (this.isPastDue()) {
                        true -> {
                            binding.serviceItemContentText.setTextColor(Color.RED)
                            binding.serviceItemContentText.setTypeface(null, Typeface.BOLD)
                            resources.getString(R.string.due).uppercase()
                        }
                        false -> {
                            TypedValue().let { value ->
                                theme.resolveAttribute(android.R.attr.textColor, value, true)
                                binding.serviceItemContentText.setTextColor(value.data)
                            }
                            binding.serviceItemContentText.setTypeface(null, Typeface.NORMAL)
                            (this.dueDate.timeInMillis - Calendar.getInstance().timeInMillis).let { timeLeftInMillis ->
                                TimeUnit.MILLISECONDS.toDays(timeLeftInMillis).let { daysLeft ->
                                    when (daysLeft) {
                                        0L -> resources.getString(R.string.tomorrow)
                                        else -> when (prefs.dueDateFormat) {
                                            DueDateFormat.DATE -> this.dueDate.getShortenedDate()
                                            DueDateFormat.WEEKS -> "${String.format("%.1f", daysLeft / MULTIPLIER_DAYS_TO_WEEKS)} weeks"
                                            DueDateFormat.MONTHS -> "${String.format("%.2f", daysLeft / MULTIPLIER_DAYS_TO_MONTHS)} months"
                                            else -> "$daysLeft ${resources.getString(R.string.days).lowercase()}"
                                        }
                                    }
                                }
                            }
                        }
                    }

                    it.root.setOnClickListener { onExpandToggle(binding, this) }
                    it.serviceItemExpandIcon.setOnClickListener { onExpandToggle(binding, this) }
                    it.serviceItemTrashIcon.setOnClickListener { onDeleteClicked(this) }
                    it.serviceItemEditIcon.setOnClickListener { onEditClicked(this) }
                }
            }
        }
    }

    private fun onExpandToggle(binding: ItemServiceBinding, service: Service) {
        binding.expanded = binding.expanded?.not() ?: false
        onItemClicked(service)
    }

    override fun getItemCount(): Int = services.size

    companion object {
        private const val MULTIPLIER_DAYS_TO_WEEKS: Float = 7.0f
        private const val MULTIPLIER_DAYS_TO_MONTHS: Float = 30.43684f
    }
}