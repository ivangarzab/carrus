package com.ivangarzab.carrus.ui.modal_service.data

import com.ivangarzab.carrus.data.models.Service

/**
 * The purpose of this file is to hold the data needed to jumpstart a
 * [ServiceBottomSheet] composable n VM'.
 */
data class ServiceModalInputData(
    val mode: ServiceModalState.Mode = ServiceModalState.Mode.NULL,
    val service: Service? = null
)
