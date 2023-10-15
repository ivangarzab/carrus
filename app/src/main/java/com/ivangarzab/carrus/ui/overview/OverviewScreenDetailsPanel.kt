package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenDetailsPanel(
    licenseState: String,
    licenseNo: String,
    vinNo: String,
    tirePressure: String,
    totalMiles: String,
    milesPerGalCity: String,
    milesPerGalHighway: String
) {
    
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun OverviewScreenDetailsPanelPreview() {
    AppTheme {
        OverviewScreenDetailsPanel(
            licenseState = "TX",
            licenseNo = "DH9 L474",
            vinNo = "ABCDEFGHIJKLMNOPQ",
            tirePressure = "32",
            totalMiles = "100,000",
            milesPerGalCity = "23",
            milesPerGalHighway = "30"
        )
    }
}