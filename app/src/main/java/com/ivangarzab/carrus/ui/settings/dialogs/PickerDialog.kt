package com.ivangarzab.carrus.ui.settings.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.ui.compose.BaseDialog
import com.ivangarzab.carrus.ui.compose.Picker
import com.ivangarzab.carrus.ui.compose.PositiveAndNegativeButtons
import com.ivangarzab.carrus.ui.compose.rememberPickerState

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PickerDialog(
    modifier: Modifier = Modifier,
    items: List<String> = (1..6).map { it.toString() },
    startIndex: Int = 0,
    visibleItemCount: Int = 3,
    onOptionSelected: (String) -> Unit = { },
    onDismissed: () -> Unit = { }
) {
    val currentItemState = rememberPickerState()

    BaseDialog(
        modifier = modifier,
        onDismissed = onDismissed
    ) {
        Picker(
            state = currentItemState,
            items = items,
            startIndex = startIndex,
            visibleItemsCount = visibleItemCount
        )
        PositiveAndNegativeButtons(
            modifier = Modifier.padding(top = 16.dp),
            onPositiveButtonClicked = { onOptionSelected(currentItemState.selectedItem) },
            onNegativeButtonClicked = onDismissed
        )
    }
}