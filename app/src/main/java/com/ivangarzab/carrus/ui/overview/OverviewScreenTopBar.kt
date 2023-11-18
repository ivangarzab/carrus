package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import timber.log.Timber
import java.lang.Float.min

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OverviewScreenTopBar(
    title: String,
    imageUri: String?,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    addTestMessage: () -> Unit = { }
) {
    val topbarColor = when (isSystemInDarkTheme()) {
        true -> MaterialTheme.colorScheme.surface
        false -> MaterialTheme.colorScheme.primary
    }

    AppTheme {
        ConstraintLayout {
            val image: ConstrainedLayoutReference = createRef()
            val topbar: ConstrainedLayoutReference = createRef()
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = imageUri)
                    .size(Size.ORIGINAL) // Set the target size to load the image at.
                    .listener { request, result ->
                        Timber.d("Image request result: $result")
                    }
                    .build()
            )

            if (painter.state is AsyncImagePainter.State.Success) {
                Timber.d("imageUri=$imageUri")
                Image(
                    modifier = Modifier
                        .constrainAs(image) {
                            start.linkTo(topbar.start)
                            end.linkTo(topbar.end)
                            bottom.linkTo(topbar.bottom)
                        }
                        .fillMaxWidth()
                        .graphicsLayer {
                            with(scrollBehavior.state) {
                                Timber.d("heightOffset=%${heightOffset} | heightOffsetLimit=%${heightOffsetLimit}")
                                alpha = 1f - ((heightOffset / heightOffsetLimit) * 1.2f)
                            }
                        },
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(topbarColor, BlendMode.Color),
                    contentDescription = "Top App Bar background image",
                )
            }
            if (imageUri != null) {
                LargeTopAppBar(
                    modifier = Modifier
                        .constrainAs(topbar) { /* No-op */ }
                        .combinedClickable(
                            onClick = { },
                            onLongClick = addTestMessage
                        ),
                    title = {
                        Column {
                            Text(
                                modifier = Modifier,
                                text = title,
                                color = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = with(scrollBehavior.state) {
                            topbarColor.copy(
                                alpha = min(1f, (heightOffset / heightOffsetLimit) + 0.4f)
                            )
                        }
                    ),
                    actions = actions,
                    scrollBehavior = scrollBehavior
                )
            } else {
                MediumTopAppBar(
                    modifier = Modifier
                        .constrainAs(topbar) { /* No-op */ }
                        .combinedClickable(
                            onClick = { },
                            onLongClick = addTestMessage
                        ),
                    title = {
                        Column {
                            Text(
                                modifier = Modifier,
                                text = title,
                                color = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = topbarColor),
                    actions = actions,
                    scrollBehavior = scrollBehavior
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenTopBarPreview() {
    AppTheme {
        OverviewScreenTopBar(
            title = "Test Top App Bar Title",
            imageUri = null,
            actions = { },
            addTestMessage = { }
        )
    }
}