package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    isVisible: Boolean = true,
    selectedIndex: Int = 0,
    onSortRequest: (SortingCallback.SortingType) -> Unit = { }
) {
    val itemStartPadding: Dp = 4.dp
    AppTheme {
        AnimatedVisibility(
            visible = isVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                modifier = modifier,
            ) {
                SortingChipButton(
                    modifier = Modifier,
                    isSelected = selectedIndex == 0,
                    onClick = { onSortRequest(SortingCallback.SortingType.NONE) },
                    text = stringResource(id = R.string.none)
                )
                SortingChipButton(
                    modifier = Modifier.padding(start = itemStartPadding),
                    isSelected = selectedIndex == 1,
                    onClick = { onSortRequest(SortingCallback.SortingType.NAME) },
                    text = stringResource(id = R.string.name)
                )
                SortingChipButton(
                    modifier = Modifier.padding(start = itemStartPadding),
                    isSelected = selectedIndex == 2,
                    onClick = { onSortRequest(SortingCallback.SortingType.DATE) },
                    text = stringResource(id = R.string.date)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SortingChipButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = { },
    text: String = "chip"
) {
    AppTheme {
        FilterChip(
            modifier = modifier,
            selected = isSelected,
            onClick = onClick,
            colors = FilterChipDefaults.filterChipColors(
                labelColor = MaterialTheme.colorScheme.primary,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            ),
            label = {
                Text(
                    text = text,
                    maxLines = 2
                )
            }
        )
    }
}