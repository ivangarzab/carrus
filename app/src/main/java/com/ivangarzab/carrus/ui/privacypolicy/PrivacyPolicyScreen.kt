package com.ivangarzab.carrus.ui.privacypolicy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.TopBar

/**
 * Created by Ivan Garza Bermea.
 */

@Composable
fun PrivacyPolicyScreen(
    url: String,
    onBackButtonClicked: () -> Unit = { }
) {
    val state = rememberWebViewState(url)

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(
                modifier = Modifier,
                title = stringResource(id = R.string.privacy_policy),
                isNavigationIconEnabled = true,
                onNavigationIconClicked = onBackButtonClicked
            )
        }
    ) {
        Box(modifier = Modifier.padding(paddingValues = it)) {
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = loadingState.progress,
                )
            }

            WebView(
                modifier = Modifier.fillMaxSize(),
                state = state,
                captureBackPresses = false
            )
        }
    }
}