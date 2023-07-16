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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.PositiveButton
import com.ivangarzab.carrus.ui.compose.TopBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.ui.create.data.CarModalStatePreview
import java.util.Locale

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun CreateScreenStateful(
    viewModel: CreateViewModel = viewModel(),
    onBackPressed: () -> Unit,
    onImportClicked: () -> Unit,
    onAddImageClicked: () -> Unit
) {
    val state: CarModalState by viewModel
        .state
        .observeAsState(initial = CarModalState())

    AppTheme {
        CreateScreen(
            state = state,
            onBackPressed = { onBackPressed() },
            onImportClicked = { onImportClicked() },
            onAddImageClicked = { onAddImageClicked() },
            onActionButtonClicked = {

            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateScreen(
    @PreviewParameter(CarModalStatePreview::class) state: CarModalState,
    onBackPressed: () -> Unit = { },
    onImportClicked: () -> Unit = { },
    onAddImageClicked: () -> Unit = { },
    onActionButtonClicked: () -> Unit = { }
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = state.title,
                    isNavigationIconEnabled = true,
                    onNavigationIconClicked = { onBackPressed() },
                    isActionIconEnabled = true,
                    onActionIconClicked = { onImportClicked() }
                )
            }
        ) { paddingValues ->
            CreateScreenContent(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onAddImageClicked = { onAddImageClicked() },
                actionButtonText = state.actionButton,
                onActionButtonClicked = { onActionButtonClicked() }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(CarModalStatePreview::class) state: CarModalState,
    onAddImageClicked: () -> Unit = { },
    actionButtonText: String = "SUBMIT",
    onUpdateAllData: () -> Unit = { },
    onActionButtonClicked: () -> Unit = { }
) {
    val verticalSeparation: Dp = 12.dp
    val spaceInBetween: Dp = 8.dp
    var isExpanded: Boolean by rememberSaveable {
        mutableStateOf(value = false)
    }

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
                    text = stringResource(id = R.string.add_a_photo),
                    onClick = { onAddImageClicked() }
                )
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation),
                    label = stringResource(id = R.string.make),
                    isRequired = true
                )
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation),
                    label = stringResource(id = R.string.model),
                    isRequired = true
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation)
                ) {
                    CreateNumberField(
                        modifier = Modifier
                            .padding(end = spaceInBetween)
                            .weight(2f),
                        label = stringResource(id = R.string.year),
                        isRequired = true
                    )
                    CreateTextField(
                        modifier = Modifier
                            .padding(start = spaceInBetween)
                            .weight(3f),
                        label = stringResource(id = R.string.license_no),
                    )
                }
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation),
                    label = stringResource(id = R.string.nickname),
                    isLastField = isExpanded.not()
                )

                if (isExpanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = verticalSeparation)
                    ) {
                        CreateNumberField(
                            modifier = Modifier
                                .padding(end = spaceInBetween)
                                .weight(3f),
                            label = stringResource(id = R.string.total_miles),
                        )
                        CreateNumberField(
                            modifier = Modifier
                                .padding(start = spaceInBetween)
                                .weight(2f),
                            label = stringResource(id = R.string.miles_per_gallon),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = verticalSeparation)
                    ) {
                        CreateNumberField(
                            modifier = Modifier
                                .padding(end = spaceInBetween)
                                .weight(2f),
                            label = stringResource(id = R.string.tire_pressure),
                        )
                        CreateTextField(
                            modifier = Modifier
                                .padding(start = spaceInBetween)
                                .weight(3f),
                            label = stringResource(id = R.string.vin_no),
                            isLastField = true
                        )
                    }
                }

                BigButton(
                    modifier = Modifier.padding(top = verticalSeparation),
                    text = stringResource(id = if (isExpanded) {
                        R.string.less_details
                    } else {
                        R.string.more_details
                    }),
                    onClick = {
                        isExpanded = isExpanded.not()
                    }
                )
                PositiveButton(
                    modifier = Modifier.padding(top = verticalSeparation),
                    text = actionButtonText,
                    onClick = { onActionButtonClicked() }
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BaseCreateField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    isRequired: Boolean = false,
    isLastField: Boolean = false,
    keyboardOptions: KeyboardOptions? = null,
    updateListener: () -> Unit = { }
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    AppTheme {
        OutlinedTextField(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            value = text,
            label = {
                Text(text = if (isRequired) "$label*" else label)
            },
            onValueChange = {
                text = it
                updateListener
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
private fun CreateTextField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    isRequired: Boolean = false,
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
            isRequired = isRequired,
            isLastField = isLastField
        )
    }
}

@Composable
private fun CreateNumberField(
    modifier: Modifier = Modifier,
    label: String = "Test field",
    isRequired: Boolean = false,
    isLastField: Boolean = false
) {
    AppTheme {
        BaseCreateField(
            modifier = modifier,
            label = label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isRequired = isRequired,
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