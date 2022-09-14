package com.ivangarzab.carbud.extensions

import android.content.res.Resources
import com.ivangarzab.carbud.R
import com.ivangarzab.carbud.data.Service
import com.ivangarzab.carbud.databinding.ModalComponentBinding
import java.util.*

/**
 * Created by Ivan Garza Bermea.
 */

fun ModalComponentBinding.bind(
    resources: Resources,
    onSave: (Service) -> Unit
) {
    this.title = resources.getString(R.string.new_component)
    this.setSaveClickListener {
        with(Service(
            name = this.componentModalNameField.text.toString(),
            lastDate = Calendar.getInstance(),
            dueDate = Calendar.getInstance().apply { timeInMillis = 1667286420000 }
        )) {
            componentModalNameField.text.clear()
            onSave(this)
        }
    }
}