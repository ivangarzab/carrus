package com.ivangarzab.carbud.modals

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.databinding.ModalComponentBinding
import com.ivangarzab.carbud.extensions.dismissKeyboard
import com.ivangarzab.carbud.extensions.toast
import com.ivangarzab.carbud.overview.OverviewViewModel
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */
class NewServiceDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: OverviewViewModel by activityViewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private lateinit var binding: ModalComponentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ModalComponentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setSaveClickListener {
                val name = componentModalNameField.text.toString()
                when (viewModel.verifyServiceData(
                    name = name
                )) {
                    true -> {
                        viewModel.onServiceCreated(
                            Service(
                                name = name,
                                lastDate = Calendar.getInstance().apply {
                                    timeInMillis = viewModel.datesInMillis.first
                                },
                                dueDate = Calendar.getInstance().apply {
                                    timeInMillis = viewModel.datesInMillis.second
                                }
                            )
                        )
                        this@NewServiceDialogFragment.dismiss()
                    }
                    false -> toast("Missing required data")
                }
            }
            setLastDateClickListener {
                showDatePickerDialog(
                    date = Calendar.getInstance()
                ) { year, month, day ->
                    viewModel.datesInMillis = Pair(
                        first = Calendar.getInstance().apply {
                            set(year, month, day)
                        }.timeInMillis,
                        second = viewModel.datesInMillis.second
                    )
                    componentModalLastDateField.setText(
                        getString(R.string.service_date_format, day, month, year)
                    )
                }
            }
            setDueDateClickListener {
                showDatePickerDialog(
                    date = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_MONTH, 7)
                    }
                ) { year, month, day ->
                    viewModel.datesInMillis = Pair(
                        first = viewModel.datesInMillis.first,
                        second = Calendar.getInstance().apply {
                            set(year, month, day)
                        }.timeInMillis
                    )
                    componentModalDueDateField.setText(
                        getString(R.string.service_date_format, day, month, year)
                    )
                }
            }
        }
    }

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

    override fun onStop() {
        super.onStop()
        dismissKeyboard(binding.root)
    }

    companion object {
        private const val NO_THEME_RES: Int = 0
    }
}