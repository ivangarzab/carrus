package com.ivangarzab.carrus.ui.create

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.PositiveButton
import com.ivangarzab.carrus.ui.compose.TopBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import java.util.Locale

/**
 * Created by Ivan Garza Bermea.
 */
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CreateScreen(
    title: String = "Create New Car",
    onBackPressed: () -> Unit = { }
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = title,
                    isNavigationIconEnabled = true,
                    onNavigationIconClicked = { onBackPressed() },
                    isActionIconEnabled = true,
                    onActionIconClicked = { }
                )
            }
        ) { paddingValues ->
            CreateScreenContent(modifier = Modifier.padding(paddingValues))
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CreateScreenContent(
    modifier: Modifier = Modifier
) {
    val separation: Dp = 12.dp
    AppTheme {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                BigButton(
                    text = stringResource(id = R.string.add_a_photo)
                )
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = separation),
                    label = stringResource(id = R.string.make),
                )
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = separation),
                    label = stringResource(id = R.string.model),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = separation)
                ) {
                    CreateNumberField(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(2f),
                        label = stringResource(id = R.string.year),
                    )
                    CreateTextField(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(3f),
                        label = stringResource(id = R.string.license_no),
                    )
                }
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = separation),
                    label = stringResource(id = R.string.nickname),
                    isLastField = true
                )
                BigButton(
                    modifier = Modifier.padding(top = separation),
                    text = stringResource(id = R.string.more_details)
                )
                PositiveButton(
                    modifier = Modifier.padding(top = separation),
                    text = stringResource(id = R.string.update)
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BaseCreateField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    isLastField: Boolean = false,
    keyboardOptions: KeyboardOptions? = null
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    AppTheme {
        OutlinedTextField(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            value = text,
            label = { Text(text = label) },
            onValueChange = { text = it },
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
    isLastField: Boolean = false
) {
    AppTheme {
        BaseCreateField(
            modifier = modifier,
            label = label,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text
            ),
            isLastField = isLastField
        )
    }
}

@Composable
fun CreateNumberField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    isLastField: Boolean = false
) {
    AppTheme {
        BaseCreateField(
            modifier = modifier,
            label = label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isLastField = isLastField
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BigButton(
    modifier: Modifier = Modifier,
    text: String = "Big button",
    onClick: () -> Unit = { }
) {
    AppTheme {
        OutlinedButton(
            modifier = modifier
                .fillMaxWidth(),
            onClick = { onClick() }
        ) {
            Text(text = text.capitalize(Locale.ROOT))
        }
    }
}