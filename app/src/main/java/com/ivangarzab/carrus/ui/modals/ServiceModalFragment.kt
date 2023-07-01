package com.ivangarzab.carrus.ui.modals

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.databinding.ModalServiceBinding
import com.ivangarzab.carrus.ui.overview.ModalServiceState
import com.ivangarzab.carrus.util.extensions.dismissKeyboard
import com.ivangarzab.carrus.util.extensions.getShortenedDate
import com.ivangarzab.carrus.util.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar
import java.util.UUID

/**
 * Created by Ivan Garza Bermea.
 */
@AndroidEntryPoint
class ServiceModalFragment : BottomSheetDialogFragment() {

    private val viewModel: ServiceModalViewModel by viewModels()

    private lateinit var binding: ModalServiceBinding

    private val args: ServiceModalFragmentArgs by navArgs()

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
        with(viewModel) {
            setArgsData(args.service)
            state.observe(viewLifecycleOwner) { state ->
                state.data?.let { setupContent(it) }
            }

            onDataRequest.observe(viewLifecycleOwner) { dataRequest ->
                if (dataRequest.first) {
                    onSubmitData(
                        when (dataRequest.second.type) {
                            ServiceModalViewModel.Type.CREATE -> getServiceFromContent()
                            ServiceModalViewModel.Type.EDIT -> getServiceFromContent(
                                dataRequest.second.id
                            )
                        }
                    )
                }
            }
            onSubmission.observe(viewLifecycleOwner) { submitSuccess ->
                when (submitSuccess) {
                    true -> findNavController().navigate(
                        ServiceModalFragmentDirections.actionNewServiceModalToOverviewFragment()
                    )
                    false -> toast("Missing required data")
                }
            }
        }

        binding.apply {
            setActionClickListener {
                Timber.v("Got action button click action")
                viewModel.onActionButtonClicked()
            }
            setRepairDateClickListener {
                Timber.v("Got repair date button click action")
                showRepairDatePickerDialog()
            }
            setDueDateClickListener {
                Timber.v("Got due date button click action")
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
            name = it.serviceModalNameInput.text.toString(),
            repairDate = Calendar.getInstance().apply {
                timeInMillis = viewModel.datesInMillis.first
            },
            dueDate = Calendar.getInstance().apply {
                timeInMillis = viewModel.datesInMillis.second
            },
            brand = it.serviceModalBrandInput.text.toString(),
            type = it.serviceModalTypeInput.text.toString(),
            cost = it.serviceModalPriceInput.text?.toString()?.takeIf { nonNullString ->
                nonNullString.isNotEmpty()
            }?.run {
                toFloat()
            } ?: DEFAULT_PRICE
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
                binding.serviceModalRepairDateInput.setText(
                    getString(R.string.service_date_format, month, day, year)
                )
            }
        )

    private fun showDueDatePickerDialog() =
        showDatePickerDialog(
            date = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, DEFAULT_DUE_DATE_ADDITION)
            },
            onDateSelected = { year, month, day ->
                viewModel.datesInMillis = Pair(
                    first = viewModel.datesInMillis.first,
                    second = Calendar.getInstance().apply {
                        set(year, month, day)
                    }.timeInMillis
                )
                binding.serviceModalDueDateInput.setText(
                    getString(R.string.service_date_format, month, day, year)
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
        private const val DEFAULT_PRICE: Float = 0.00f
        private const val DEFAULT_DUE_DATE_ADDITION: Int = 7
    }
}