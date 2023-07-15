package com.ivangarzab.carrus.ui.settings.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsStatePreview :
    PreviewParameterProvider<SettingsState> {
    override val values = sequenceOf(
        SettingsState(
            car = Car.empty,
            alarmTime = DEFAULT_ALARM_TIME.toString(),
        )
    )
}