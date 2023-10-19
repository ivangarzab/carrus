package com.ivangarzab.carrus.ui.overview.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivangarzab.carrus.data.models.Car

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewStatePreviewProvider : PreviewParameterProvider<OverviewState> {
    override val values = sequenceOf(
        OverviewState(car = Car.default)
    )
}