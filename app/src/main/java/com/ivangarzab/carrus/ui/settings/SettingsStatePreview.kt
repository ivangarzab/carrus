package com.ivangarzab.carrus.ui.settings

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.data.repositories.DEFAULT_ALARM_TIME

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsStatePreview :
    PreviewParameterProvider<SettingsViewModel.SettingsState> {
    override val values = sequenceOf(
        SettingsViewModel.SettingsState(
            car = Car.empty,
            alarmTime = DEFAULT_ALARM_TIME.toString(),
        )
    )
}