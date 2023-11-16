package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ivangarzab.carrus.ui.compose.fadingEdge
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
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

            imageUri?.let {
                AsyncImage(
                    modifier = Modifier
                        .constrainAs(image) {
                            bottom.linkTo(topbar.bottom)
                        }
                        .fillMaxWidth()
                        .graphicsLayer {
                            val scrollState = scrollBehavior.state
                            alpha = 1f - ((scrollState.heightOffset /
                                    scrollState.heightOffsetLimit) * 1.2f)
                            translationY = 0.2f * scrollState.heightOffset
                        },
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(data = imageUri)
                        .build(),
                    contentScale = ContentScale.Crop, //TODO: Create custom ContentScale
                    contentDescription = "Top App Bar background image"
                )
            }
            LargeTopAppBar(
                modifier = Modifier
                    .constrainAs(topbar) { /* No-op */ }
                    .fadingEdge(
                        Brush.verticalGradient(
                            0.90f to topbarColor,
                            0.95f to topbarColor.copy(
                                alpha = 0.90f
                            ),
                            1.0f to topbarColor.copy(
                                alpha = 0.80f
                            )
                        )
                    )
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
                    containerColor = imageUri?.let {
                        val scrollState = scrollBehavior.state
                        topbarColor.copy(
                            alpha = min(
                                1f,
                                (scrollState.heightOffset / scrollState.heightOffsetLimit)
                                        + 0.4f
                            )
                        )
                    } ?: topbarColor
                ),
                actions = actions,
                scrollBehavior = scrollBehavior
            )
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