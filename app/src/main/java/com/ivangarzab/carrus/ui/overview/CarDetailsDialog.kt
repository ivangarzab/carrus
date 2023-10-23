package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.BaseDialog
import com.ivangarzab.carrus.ui.compose.BigNegativeButton

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
@Deprecated("This is no longer in use")
fun CarDetailsDialog(
    vinNo: String = "I123456789ABCDEFG",
    tirePressure: String = "32",
    milesTotal: String = "250000",
    milesPerGallon: String = "26",
    onClick: () -> Unit = { }
) {
    BaseDialog(
        onDismissed = onClick,
        isLarge = true
    ) {
        Text(
            text = "Details",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 24.dp),
            labelText = stringResource(id = R.string.total_miles),
            contentText = milesTotal + " " + stringResource(id = R.string.miles)
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 8.dp),
            labelText = "Averaged",
            contentText = milesPerGallon + " " + stringResource(id = R.string.miles_per_gal)
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 8.dp),
            labelText = stringResource(id = R.string.tire_pressure),
            contentText = tirePressure + " " + stringResource(id = R.string.psi)
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 8.dp),
            labelText = stringResource(id = R.string.vin_no),
            contentText = vinNo
        )
        BigNegativeButton(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(id = R.string.ok),
            onClick = onClick
        )
    }
}

@Composable
fun CarDetailsDialogRow(
    modifier: Modifier = Modifier,
    labelText: String,
    contentText: String
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .weight(weight = 3f),
            text = labelText,
            textAlign = TextAlign.Left
        )
        Text(
            modifier = Modifier
                .weight(weight = 6f, fill = true),
            text = contentText,
            textAlign = TextAlign.End
        )
    }
}