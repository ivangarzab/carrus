package com.ivangarzab.carrus.ui.overview.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Created by Ivan Garza Bermea.
 */
class ModalServiceStatePreviewProvider: PreviewParameterProvider<ModalServiceState> {
    override val values = sequenceOf(
        ModalServiceState(
            name = "Test Service",
            repairDate = "07/25/23",
            dueDate = "09/12/23",
            brand = "Test brand",
            type = "Test type",
            price = "99.99"
        )
    )
}