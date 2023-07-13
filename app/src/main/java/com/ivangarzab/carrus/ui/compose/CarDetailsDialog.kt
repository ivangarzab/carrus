package com.ivangarzab.carrus.ui.compose

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

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CarDetailsDialog(
    vinNo: String = "---",
    tirePressure: String = "---",
    milesTotal: String = "---",
    milesPerGallon: String = "---",
    onClick: () -> Unit = { }
) {
    InfoDialog {
        Text(
            text = "Details",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 24.dp),
            labelText = stringResource(id = R.string.vin_no),
            contentText = vinNo
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 8.dp),
            labelText = stringResource(id = R.string.total_miles),
            contentText = milesTotal
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 8.dp),
            labelText = stringResource(id = R.string.miles_per_gallon),
            contentText = milesPerGallon
        )
        CarDetailsDialogRow(
            modifier = Modifier.padding(top = 8.dp),
            labelText = stringResource(id = R.string.total_miles),
            contentText = milesTotal
        )
        NegativeButton(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.ok)
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
                .weight(weight = 1f, fill = true)
                .padding(start = 8.dp),
            text = labelText,
            textAlign = TextAlign.Left
        )
        Text(
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .padding(end = 8.dp),
            text = contentText,
            textAlign = TextAlign.End
        )
    }
}