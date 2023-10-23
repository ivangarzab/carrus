package com.ivangarzab.carrus.ui.compose

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.ui.compose.theme.Typography

/**
 * Created by Ivan Garza Bermea.
 */
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