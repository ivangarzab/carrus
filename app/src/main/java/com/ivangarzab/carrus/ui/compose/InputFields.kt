package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BaseCreateField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    keyboardOptions: KeyboardOptions? = null,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        OutlinedTextField(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            value = content ?: label,
            label = {
                Text(text = if (isRequired) "$label*" else label)
            },
            onValueChange = {
                updateListener(it.trim())
            },
            singleLine = true,
            keyboardOptions = (keyboardOptions ?: KeyboardOptions.Default).copy(
                imeAction = if (isLastField) {
                    ImeAction.Done
                } else {
                    ImeAction.Next
                }
            )
        )
    }
}
@Composable
fun CreateTextField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        BaseCreateField(
            modifier = modifier,
            label = label,
            content = content,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text
            ),
            isRequired = isRequired,
            isLastField = isLastField,
            updateListener = updateListener
        )
    }
}

@Composable
fun CreateNumberField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        BaseCreateField(
            modifier = modifier,
            label = label,
            content = content,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isRequired = isRequired,
            isLastField = isLastField,
            updateListener = updateListener
        )
    }
}