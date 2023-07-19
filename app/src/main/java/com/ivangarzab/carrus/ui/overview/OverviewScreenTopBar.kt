package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenTopBar(
    title: String = "Test Top App Bar Title",
    imageUri: Uri? = null,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
) {
    AppTheme {
        imageUri?.let {
            Image(
                modifier = Modifier
                    .height(152.dp)
                    .fillMaxWidth(),
                painter = //painterResource(id = R.drawable.image_default_background)
                rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(data = imageUri)
                        .build()
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Car image"
            )
        }
        LargeTopAppBar(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                ),
            title = {
                Text(
                    modifier = Modifier,
                    text = title,
                    color = imageUri?.let {
                        Color.White
                    } ?: MaterialTheme.colorScheme.onSurface
                )
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = imageUri?.let {
                    Color.Transparent
                } ?: MaterialTheme.colorScheme.surface
            ),
            actions = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Action item",
                    tint = imageUri?.let {
                        Color.White
                    } ?: MaterialTheme.colorScheme.onSurface
                )
            },
            scrollBehavior = scrollBehavior
        )
    }
}