package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.compose.theme.Typography

/**
 * This file contains the most common and basic Composables for use across the application.
 *
 * Created by Ivan Garza Bermea.
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    onDismissed: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit = { }
) {
    AppTheme {
        Dialog(
            onDismissRequest = onDismissed,
            properties = if (isLarge) {
                DialogProperties(usePlatformDefaultWidth = false)
            } else {
                DialogProperties()
            }
        ) {
            Card(modifier = Modifier.padding(start = 12.dp, end = 12.dp)) {
                Column(
                    modifier = modifier.padding(
                        if (isLarge) 12.dp else 28.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    onConfirmationResult: (Boolean) -> Unit = { },
    text: String = "Delete car data?"
) {
    BaseDialog(
        modifier = modifier,
        onDismissed = { onConfirmationResult(false) }
    ) {
        Text(
            modifier = Modifier,
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        PositiveAndNegativeButtons(
            modifier = Modifier.padding(top = 16.dp),
            positiveButtonText = stringResource(id = R.string.yes),
            onPositiveButtonClicked = { onConfirmationResult(true) },
            negativeButtonText = stringResource(id = R.string.no),
            onNegativeButtonClicked = { onConfirmationResult(false) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String = "App Bar Title",
    isNavigationIconEnabled: Boolean = false,
    onNavigationIconClicked: () -> Unit = { },
    isActionIconEnabled: Boolean = false,
    onActionIconClicked: () -> Unit = { },
    action: @Composable () -> Unit = { }
) {
    AppTheme {
        TopAppBar(
            modifier = modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = title,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                )
            },
            colors = if (isSystemInDarkTheme()) {
                TopAppBarDefaults.topAppBarColors()
            } else {
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            },
            navigationIcon = {
                if (isNavigationIconEnabled) {
                    IconButton(onClick = { onNavigationIconClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back arrow",
                            tint = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.onBackground
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    }
                }
            },
            actions = {
                if (isActionIconEnabled) {
                    Text(
                        modifier = Modifier
                            .clickable { onActionIconClicked() },
                        text = "IMPORT",
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun PanelTitleText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        style = Typography.titleMediumLarge,
        fontStyle = FontStyle.Italic
    )
}

@Composable
fun PanelIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String = "Panel icon button"
) {
    PanelIcon(
        modifier = modifier,
        onClick = onClick,
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription
    )
}

@Composable
fun PanelIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    painter: Painter,
    contentDescription: String = "Panel icon button"
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            painter = painter,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = contentDescription
        )
    }
}
