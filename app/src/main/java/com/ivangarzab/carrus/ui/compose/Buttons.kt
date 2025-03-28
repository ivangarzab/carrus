package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
            shape = RoundedCornerShape(16.dp),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
            shape = RoundedCornerShape(16.dp),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NeutralButton(
    modifier: Modifier = Modifier,
    text: String = "Not Now",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = { onClick() },
        ) {
            Text(text = text)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PositiveAndNegativeButtons(
    modifier: Modifier = Modifier,
    positiveButtonText: String = stringResource(id = R.string.submit),
    onPositiveButtonClicked: () -> Unit = { },
    negativeButtonText: String = stringResource(id = R.string.cancel),
    onNegativeButtonClicked: () -> Unit = { }
) {
    Column(modifier = modifier) {
        PositiveButton(
            modifier = Modifier,
            text = positiveButtonText,
            onClick = onPositiveButtonClicked
        )
        NegativeButton(
            modifier = Modifier.padding(top = 4.dp),
            text = negativeButtonText,
            onClick = onNegativeButtonClicked
        )
    }
}

private val bigButtonHeight = 48.dp
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BigPositiveButton(
    modifier: Modifier = Modifier,
    text: String = "Not Now",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        PositiveButton(
            modifier = modifier.height(bigButtonHeight),
            text = text,
            onClick = onClick
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BigNegativeButton(
    modifier: Modifier = Modifier,
    text: String = "Not Now",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        NegativeButton(
            modifier = modifier.height(bigButtonHeight),
            text = text,
            onClick = onClick
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BigNeutralButton(
    modifier: Modifier = Modifier,
    text: String = "Not Now",
    onClick: () -> Unit = {  }
) {
    AppTheme {
        NeutralButton(
            modifier = modifier.height(bigButtonHeight),
            text = text,
            onClick = onClick
        )
    }
}