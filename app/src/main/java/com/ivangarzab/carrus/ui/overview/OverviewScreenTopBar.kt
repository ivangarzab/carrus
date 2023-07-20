package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ivangarzab.carrus.R
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
    imageUri: String? = null,
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
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
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(clipSpecs)
                        .constrainAs(image) {
                            bottom.linkTo(topbar.bottom)
                        }
                        .graphicsLayer {
                            alpha = 1f - ((scrollBehavior.state.heightOffset /
                                    scrollBehavior.state.heightOffsetLimit) * 1.2f)
                            translationY = scrollBehavior.state.heightOffset
                        },
                    painter = rememberAsyncImagePainter(
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(data = imageUri)
                            .build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Top App Bar background image"
                )
            }
            LargeTopAppBar(
                modifier = Modifier
                    .constrainAs(topbar) { }
                    .clip(clipSpecs),
                title = {
                    Text(
                        modifier = Modifier
                            .alpha(1.0f),
                        text = title,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) {
                        imageUri?.let {
                            colorResource(id = R.color.black_80_percent)
                        } ?: MaterialTheme.colorScheme.surface
                    } else {
                        imageUri?.let {
                            colorResource(id = R.color.indigo_40_percent)
                        } ?: MaterialTheme.colorScheme.primary
                    }
                ),
                actions = { },
                scrollBehavior = scrollBehavior
            )
        }
    }
}