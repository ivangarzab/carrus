package com.ivangarzab.carrus.ui.compose.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivangarzab.carrus.ui.modals.ServiceModalState

/**
 * Created by Ivan Garza Bermea.
 */
class ServiceModalStatePreviewProvider : PreviewParameterProvider<ServiceModalState> {
    override val values = sequenceOf(
        ServiceModalState(
            name = "Test Service",
            repairDate = "9/12/92",
            dueDate = "9/99/99",
            brand = "Some brand",
            type = "The brand's type"
        )
    )
}