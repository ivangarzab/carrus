package com.ivangarzab.carrus.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BaseInputField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    prefix: String? = null,
    isReadOnly: Boolean = false,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    textStyle: TextStyle? = null,
    keyboardOptions: KeyboardOptions? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        OutlinedTextField(
            modifier = modifier,
            value = content ?: label,
            prefix = {
                prefix?.let {
                    Text(
                        text = it,
                        style = textStyle ?: LocalTextStyle.current
                    )
                }
            },
            label = {
                Text(text = if (isRequired) "$label*" else label)
            },
            textStyle = textStyle ?: LocalTextStyle.current,
            onValueChange = {
                updateListener(
                    it.length.let { len ->
                        if (it.takeLast(2) == "  ") {
                            it.trim().plus(" ")
                        } else {
                            it.trimStart()
                        }
                    }
                )
            },
            singleLine = true,
            readOnly = isReadOnly,
            keyboardOptions = (keyboardOptions ?: KeyboardOptions.Default).copy(
                imeAction = if (isLastField) {
                    ImeAction.Done
                } else {
                    ImeAction.Next
                }
            ),
            leadingIcon = leadingIcon,
            interactionSource = interactionSource ?: remember { MutableInteractionSource() }
        )
    }
}
@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        BaseInputField(
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
fun NumberInputField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        BaseInputField(
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

@Composable
fun MoneyInputField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    content: String? = null,
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    updateListener: (String) -> Unit = { }
) {
    AppTheme {
        BaseInputField(
            modifier = modifier,
            label = label,
            content = content,
            prefix = "$",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isRequired = isRequired,
            isLastField = isLastField,
            updateListener = updateListener
        )
    }
}

@Composable
fun CalendarInputField(
    modifier: Modifier = Modifier,
    label: String = "calendar date",
    content: String? = "09/12/1992",
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    updateListener: (String) -> Unit = { },
    onClick: () -> Unit = { },
) {
    BaseInputField(
        modifier = modifier.clickable { onClick() },
        label = label,
        content = content,
        textStyle = TextStyle(fontWeight = FontWeight.SemiBold),
        keyboardOptions = KeyboardOptions(),
        isReadOnly = true,
        isRequired = isRequired,
        isLastField = isLastField,
        updateListener = updateListener,
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onClick()
                        }
                    }
                }
            },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar_event),
                contentDescription = "Calendar icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}