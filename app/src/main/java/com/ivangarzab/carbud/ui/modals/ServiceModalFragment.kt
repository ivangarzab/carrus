package com.ivangarzab.carbud.ui.modals

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.databinding.ModalServiceBinding
import com.ivangarzab.carbud.ui.overview.ModalServiceState
import com.ivangarzab.carbud.util.extensions.dismissKeyboard
import com.ivangarzab.carbud.util.extensions.toast
import com.ivangarzab.carbud.ui.overview.OverviewViewModel
import com.ivangarzab.carbud.util.extensions.getShortenedDate
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class ServiceModalFragment : BottomSheetDialogFragment() {

    private val viewModel: OverviewViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private lateinit var binding: ModalServiceBinding

    private val args: ServiceModalFragmentArgs by navArgs()
    private enum class Type { CREATE, EDIT }
    private lateinit var type: Type

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModalServiceBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = args.service?.let {
            setupContent(it)
            Type.EDIT
        } ?: Type.CREATE

        binding.apply {
            setActionClickListener {
                val name = serviceModalNameField.text.toString()
                when (viewModel.verifyServiceData(
                    name = name
                )) {
                    true -> {
                        when (type) {
                            Type.CREATE -> viewModel.onServiceCreated(
                                getServiceFromContent()
                            ).also { this@ServiceModalFragment.dismiss() }
                            Type.EDIT -> args.service?.let {
                                viewModel.onServiceUpdate(
                                    getServiceFromContent(it.id)
                                ).also { this@ServiceModalFragment.dismiss() }
                            }
                        }
                    }
                    false -> toast("Missing required data")
                }
            }
            setRepairDateClickListener {
                showRepairDatePickerDialog()
            }
            setDueDateClickListener {
                showDueDatePickerDialog()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        dismissKeyboard(binding.root)
    }

    private fun setupContent(data: Service) {
        binding.data = ModalServiceState(
            name = data.name,
            repairDate = data.repairDate.getShortenedDate(),
            dueDate = data.dueDate.getShortenedDate(),
            brand = data.brand ?: "",
            type = data.type ?: "",
            price = data.cost.toString()
        )
        viewModel.datesInMillis = Pair(data.repairDate.timeInMillis, data.dueDate.timeInMillis)
    }

    private fun getServiceFromContent(
        id: String = ""
    ): Service = binding.let {
        Service(
            id = id.ifBlank { UUID.randomUUID().toString() },
            name = it.serviceModalNameField.text.toString(),
            repairDate = Calendar.getInstance().apply {
                timeInMillis = viewModel.datesInMillis.first
            },
            dueDate = Calendar.getInstance().apply {
                timeInMillis = viewModel.datesInMillis.second
            },
            brand = it.serviceModalBrandField.text.toString(),
            type = it.serviceModalTypeField.text.toString(),
            cost = it.serviceModalPriceField.text.toString().toFloat()
        )
    }

    private fun showRepairDatePickerDialog() =
        showDatePickerDialog(
            date = Calendar.getInstance(),
            onDateSelected = { year, month, day ->
                viewModel.datesInMillis = Pair(
                    first = Calendar.getInstance().apply {
                        set(year, month, day)
                    }.timeInMillis,
                    second = viewModel.datesInMillis.second
                )
                binding.serviceModalRepairDateField.setText(
                    getString(R.string.service_date_format, month, day, year)
                )
                binding.serviceModalRepairDateCalendar.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_calendar_checked)
                )
            }
        )

    private fun showDueDatePickerDialog() =
        showDatePickerDialog(
            date = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 7)
            },
            onDateSelected = { year, month, day ->
                viewModel.datesInMillis = Pair(
                    first = viewModel.datesInMillis.first,
                    second = Calendar.getInstance().apply {
                        set(year, month, day)
                    }.timeInMillis
                )
                binding.serviceModalDueDateField.setText(
                    getString(R.string.service_date_format, month, day, year)
                )
                binding.serviceModalDueDateCalendar.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_calendar_checked)
                )
            }
        )

    private fun showDatePickerDialog(date: Calendar, onDateSelected: (Int, Int, Int) -> Unit) {
        DatePickerDialog(
            requireContext(),
            NO_THEME_RES,
            { _, year, month, day ->
                onDateSelected(year, month, day)
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    companion object {
        private const val NO_THEME_RES: Int = 0
    }
}