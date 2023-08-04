package com.ivangarzab.carrus.ui.compose.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivangarzab.carrus.data.Service

/**
 * Created by Ivan Garza Bermea.
 */
class ServicePreviewProvider : PreviewParameterProvider<Service> {
    override val values = sequenceOf(
        Service.empty,
        Service.serviceList[0]
    )
}