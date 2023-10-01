package com.ivangarzab.carrus.ui.settings.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Created by Ivan Garza Bermea.
 */
class SettingsStatePreview :
    PreviewParameterProvider<SettingsState> {
    override val values = sequenceOf(
        SettingsState()
    )
}