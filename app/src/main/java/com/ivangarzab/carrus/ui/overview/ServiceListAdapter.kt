package com.ivangarzab.carrus.ui.overview

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.DueDateFormat
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.databinding.ItemServiceBinding
import com.ivangarzab.carrus.util.extensions.getDetails
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.isPastDue
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Created by Ivan Garza Bermea.
 */
class ServiceListAdapter(
    private val resources: Resources,
    private val theme: Resources.Theme,
    private val dueDateFormat: DueDateFormat,
    private var services: List<Service>
) : RecyclerView.Adapter<ServiceListAdapter.ServiceListViewHolder>() {

    var onEditClicked: ((Service) -> Unit)? = null
    var onDeleteClicked: ((Service) -> Unit)? = null

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
                        when (isPastDue()) { // TODO: This is duplicating code from generateItemServiceState()
                            true -> {
                                binding.serviceItemDueDate.setTextColor(Color.RED)
                                binding.serviceItemDueDate.setTypeface(null, Typeface.BOLD)
                            }
                            false -> {
                                TypedValue().let { value ->
                                    theme.resolveAttribute(android.R.attr.textColor, value, true)
                                    binding.serviceItemDueDate.setTextColor(value.data)
                                }
                                binding.serviceItemDueDate.setTypeface(null, Typeface.NORMAL)
                            }
                        }

                        binding.root.setOnClickListener { onExpandToggle(binding, this) }
                        binding.serviceItemExpandIcon.setOnClickListener { onExpandToggle(binding, this) }
                        binding.serviceItemTrashIcon.setOnClickListener { onDeleteClicked?.let { it(this) } }
                        binding.serviceItemEditIcon.setOnClickListener { onEditClicked?.let { it(this) } }
                    }
                }
            }
        }
    }

    fun setOnEditClickedListener(onEditClicked: (Service) -> Unit) {
        this.onEditClicked = onEditClicked
    }

    fun setOnDeleteClickedListener(onDeleteClicked: (Service) -> Unit) {
        this.onDeleteClicked = onDeleteClicked
    }

    fun updateContent(services: List<Service>) {
        this.services = services
        notifyDataSetChanged()
    }

    private fun onExpandToggle(binding: ItemServiceBinding, service: Service) {
        binding.state = binding.state?.let { state ->
            state.expanded.let {
                TransitionManager.beginDelayedTransition(binding.serviceItemInnerRoot, AutoTransition().apply {
                    duration = ITEM_ANIMATION_DURATION_MS
                    if (state.expanded) {
                        addTarget(binding.serviceItemDetails)
                        addTarget(binding.serviceItemRepairDate)
                    }
                })
                state.copy(expanded = it.not())
            }
        }
//        onItemClicked(service)
    }

    private fun generateItemServiceState(position: Int, data: Service): ItemServiceState =
        ItemServiceState(
            position = position,
            name = data.name,
            repairDateFormat = resources.getString(R.string.service_repair_date_format, data.repairDate.getShortenedDate()),
            details = data.getDetails(),
            price = resources.getString(R.string.price_money, data.cost),
            dueDateFormat = when (data.isPastDue()) {
                true -> resources.getString(R.string.due).uppercase()
                false -> {
                    (data.dueDate.timeInMillis - Calendar.getInstance().timeInMillis).let { timeLeftInMillis ->
                        TimeUnit.MILLISECONDS.toDays(timeLeftInMillis).let { daysLeft ->
                            when (daysLeft) {
                                0L -> resources.getString(R.string.tomorrow)
                                else -> when (dueDateFormat) {
                                    DueDateFormat.DATE -> data.dueDate.getShortenedDate()
                                    DueDateFormat.WEEKS -> resources.getString(R.string.service_due_date_week_format, String.format("%.1f", daysLeft / MULTIPLIER_DAYS_TO_WEEKS))
                                    DueDateFormat.MONTHS -> resources.getString(R.string.service_due_date_months_format, String.format("%.2f", daysLeft / MULTIPLIER_DAYS_TO_MONTHS))
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
        private const val ITEM_ANIMATION_DURATION_MS: Long = 200
    }
}