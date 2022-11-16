package com.ivangarzab.carbud.ui.overview

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    private val theme: Resources.Theme,
    private val services: List<Service>,
    val onItemClicked: (Service) -> Unit,
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
                binding.serviceItemName.text = this.name
                binding.serviceItemContentText.text = when (this.isPastDue()) {
                    true -> {
                        binding.serviceItemContentText.setTextColor(Color.RED)
                        binding.serviceItemContentText.setTypeface(null, Typeface.BOLD)
                        "DUE"
                    }
                    false -> {
                        TypedValue().let {
                            theme.resolveAttribute(android.R.attr.textColor, it, true)
                            binding.serviceItemContentText.setTextColor(it.data)
                        }
                        binding.serviceItemContentText.setTypeface(null, Typeface.NORMAL)
                        (this.dueDate.timeInMillis - Calendar.getInstance().timeInMillis).let { timeLeftInMillis ->
                            TimeUnit.MILLISECONDS.toDays(timeLeftInMillis).let { daysLeft ->
                                when (daysLeft) {
                                    0L -> "Tomorrow"
                                    else -> when (prefs.dueDateFormat) {
                                        DueDateFormat.DATE -> this.dueDate.getShortenedDate()
                                        DueDateFormat.WEEKS -> "${String.format("%.1f", daysLeft / MULTIPLIER_DAYS_TO_WEEKS)} weeks"
                                        DueDateFormat.MONTHS -> "${String.format("%.2f", daysLeft / MULTIPLIER_DAYS_TO_MONTHS)} months"
                                        else -> "$daysLeft days"
                                    }
                                }
                            }
                        }
                    }
                }
                binding.root.setOnClickListener { onItemClicked(this) }
                binding.serviceItemTrashIcon.setOnClickListener { onDeleteClicked(this) }
                binding.serviceItemContentImage.setOnClickListener {
                    binding.expanded = binding.expanded?.not() ?: false
                }
            }
        }
    }

    override fun getItemCount(): Int = services.size

    companion object {
        private const val MULTIPLIER_DAYS_TO_WEEKS: Float = 7.0f
        private const val MULTIPLIER_DAYS_TO_MONTHS: Float = 30.43684f
    }
}