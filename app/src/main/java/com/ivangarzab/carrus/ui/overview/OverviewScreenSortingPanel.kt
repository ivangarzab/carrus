package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenSortingPanel(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    onSortRequest: (SortingCallback.SortingType) -> Unit = { }
) {
    val itemStartPadding: Dp = 4.dp
    AppTheme {
        Row(modifier = modifier,
        ) {
            SortingButton(
                modifier = Modifier.padding(start = itemStartPadding),
                isSelected = selectedIndex == 0,
                onClick = { onSortRequest(SortingCallback.SortingType.NONE )},
                text = stringResource(id = R.string.none)
            )
            SortingButton(
                modifier = Modifier.padding(start = itemStartPadding),
                isSelected = selectedIndex == 1,
                onClick = { onSortRequest(SortingCallback.SortingType.NAME )},
                text = stringResource(id = R.string.name)
            )
            SortingButton(
                modifier = Modifier.padding(start = itemStartPadding),
                isSelected = selectedIndex == 2,
                onClick = { onSortRequest(SortingCallback.SortingType.DATE )},
                text = stringResource(id = R.string.date)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SortingButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = { },
    text: String = "button"
) {
    val selectionColor: Color by animateColorAsState(
        targetValue = when (isSelected) {
            true -> MaterialTheme.colorScheme.primary
            false -> Color.Transparent
        }
    )
    AppTheme {
        OutlinedButton(
            modifier = modifier
                .width(90.dp),
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = selectionColor
            )
        ) {
            Text(
                text = text,
                color = when (isSelected) {
                    true -> MaterialTheme.colorScheme.onPrimary
                    false -> MaterialTheme.colorScheme.primary
                },
                fontStyle = FontStyle.Italic
            )
        }
    }
}