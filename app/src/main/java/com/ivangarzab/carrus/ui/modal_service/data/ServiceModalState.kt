package com.ivangarzab.carrus.ui.modal_service.data

import com.ivangarzab.carrus.data.models.Service
import com.ivangarzab.carrus.util.extensions.getShortenedDate

/**
 * Created by Ivan Garza Bermea.
 */
data class ServiceModalState(
    val mode: Mode = Mode.NULL,
    val title: String? = null,
    val name: String? = null,
    val repairDate: String? = null,
    val dueDate: String? = null,
    val brand: String? = null,
    val type: String? = null,
    val price: String? = null
) {
    enum class Mode { NULL, CREATE, EDIT, RESCHEDULE }

    companion object {
        fun fromService(service: Service, mode: Mode): ServiceModalState {
            return ServiceModalState(
                mode = mode,
                title = "Update Service",
                name = service.name,
                repairDate = service.repairDate.getShortenedDate(),
                dueDate = service.dueDate.getShortenedDate(),
                brand = service.brand,
                type = service.type,
                price = "%.2f".format(service.cost)
            )
        }
    }
}