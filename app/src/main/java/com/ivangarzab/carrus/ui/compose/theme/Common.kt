package com.ivangarzab.carrus.ui.compose.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * This file contains the most common and basic Composables for use across the application.
 *
 * Created by Ivan Garza Bermea.
 */

@Preview
@Composable
fun PositiveButton(
    modifier: Modifier = Modifier,
    text: String = "ACTION",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun NegativeButton(
    modifier: Modifier = Modifier,
    text: String = "Not Now",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        TextButton(
            modifier = modifier
                .fillMaxWidth(),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}