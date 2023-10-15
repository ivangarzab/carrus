package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ivangarzab.carrus.App
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import java.lang.Float.min

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OverviewScreenTopBar(
    title: String,
    plates: String,
    imageUri: String?,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    addTestMessage: () -> Unit = { }
) {
    val clipSpecs = RoundedCornerShape(
        bottomStart = 32.dp,
        bottomEnd = 32.dp
    )

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
                        .clip(clipSpecs)
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
//                    .clip(clipSpecs)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            if (App
                                    .isRelease()
                                    .not()
                            ) addTestMessage()
                        }
                    ),
                title = {
                    Column {
                        val scrollState = scrollBehavior.state
                        if (scrollState.heightOffset == 0.0f) {
                            Text(
                                modifier = Modifier,
                                text = plates,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                        Text(
                            modifier = Modifier,
                            text = title,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) {
                        imageUri?.let {
                            val scrollState = scrollBehavior.state
                            MaterialTheme.colorScheme.surface.copy(
                                alpha = min(
                                    1f,
                                    (scrollState.heightOffset / scrollState.heightOffsetLimit)
                                            + 0.4f
                                )
                            )
                        } ?: MaterialTheme.colorScheme.surface
                    } else {
                        imageUri?.let {
                            val scrollState = scrollBehavior.state
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = min(
                                    1f,
                                    (scrollState.heightOffset / scrollState.heightOffsetLimit)
                                            + 0.4f
                                )
                            )
                        } ?: MaterialTheme.colorScheme.primary
                    }
                ),
                actions = actions,
                scrollBehavior = scrollBehavior
            )
            Box(modifier =  Modifier //TODO: We might want to add this at the bottom of the appbar
                .height(32.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
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
            plates = "DH9 L474",
            imageUri = null,
            actions = { },
            addTestMessage = { }
        )
    }
}