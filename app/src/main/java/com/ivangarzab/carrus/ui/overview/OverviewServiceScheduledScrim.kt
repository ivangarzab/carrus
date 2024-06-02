package com.ivangarzab.carrus.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.R
import kotlinx.coroutines.delay

@Composable
fun OverviewServiceScheduledScrim(
    modifier: Modifier = Modifier,
    text: String,
    onFinishWaiting: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.scrim.copy(
                    alpha = 0.6f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_circle),
                tint = Color.White,
                contentDescription = "Confirmation icon"
            )
            Text(
                text = text,
                color = Color.White
            )
        }
    }

    LaunchedEffect(
        key1 = text,
        block ={
            delay(500)
            onFinishWaiting()
        }
    )
}

@Preview
@Composable
fun OverviewServiceScheduledScrimPreview() {
    OverviewServiceScheduledScrim(
        text = "Service scheduled",
        onFinishWaiting = { }
    )
}