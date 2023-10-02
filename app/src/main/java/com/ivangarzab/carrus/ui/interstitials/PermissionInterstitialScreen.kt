package com.ivangarzab.carrus.ui.interstitials

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.NegativeButton
import com.ivangarzab.carrus.ui.compose.PositiveButton
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.interstitials.data.PermissionInterstitialData
import com.ivangarzab.carrus.ui.interstitials.data.PermissionInterstitialDataPreview

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PermissionInterstitialScreenContent(
    @PreviewParameter(PermissionInterstitialDataPreview::class) data: PermissionInterstitialData,
    positiveButtonClick: () -> Unit = { },
    negativeButtonClick: () -> Unit = { }
) {
    AppTheme {
        Card {
            Column(modifier = Modifier.padding(40.dp)) {
                Text(
                    text = stringResource(id = data.title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(id = data.subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(id = data.body),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                PositiveButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.goto_settings),
                    onClick = { positiveButtonClick() }
                )
                NegativeButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = { negativeButtonClick() }
                )
            }
        }
    }
}

@Composable
fun PermissionInterstitialScreen(
    viewModel: PermissionInterstitialViewModel,
    positiveButtonClick: () -> Unit = { },
    negativeButtonClick: () -> Unit = { }
) {
    val state: PermissionInterstitialData by viewModel.uiState.observeAsState(
        PermissionInterstitialData(0, 0, 0)
    )

    // the state != null is needed here for a strange bug
    if (state != null && state.title != 0 && state.subtitle != 0 && state.body != 0) {
        PermissionInterstitialScreenContent(
            data = state,
            positiveButtonClick = positiveButtonClick,
            negativeButtonClick = negativeButtonClick
        )
    }
}