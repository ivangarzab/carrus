package com.ivangarzab.carrus.ui.compose

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit = { },
    content: @Composable ColumnScope.() -> Unit = { }
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    AppTheme {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissed,
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) { content() }
    }
}