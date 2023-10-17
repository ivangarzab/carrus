package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * This file contains the most common and basic Composables for use across the application.
 *
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    onDismissed: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit = { }
) {
    AppTheme {
        Dialog(
            onDismissRequest = onDismissed,
            properties = if (isLarge) {
                DialogProperties(usePlatformDefaultWidth = false)
            } else {
                DialogProperties()
            }
        ) {
            Card(modifier = Modifier.padding(start = 12.dp, end = 12.dp)) {
                Column(
                    modifier = modifier.padding(
                        if (isLarge) 12.dp else 28.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    onConfirmationResult: (Boolean) -> Unit = { },
    text: String = "Delete car data?"
) {
    BaseDialog(
        modifier = modifier,
        onDismissed = { onConfirmationResult(false) }
    ) {
        Text(
            modifier = Modifier,
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        PositiveAndNegativeButtons(
            modifier = Modifier.padding(top = 16.dp),
            positiveButtonText = stringResource(id = R.string.yes),
            onPositiveButtonClicked = { onConfirmationResult(true) },
            negativeButtonText = stringResource(id = R.string.no),
            onNegativeButtonClicked = { onConfirmationResult(false) }
        )
    }
}
