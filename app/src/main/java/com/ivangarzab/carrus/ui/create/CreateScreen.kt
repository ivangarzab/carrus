package com.ivangarzab.carrus.ui.create

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ivangarzab.carrus.App.Companion.isRelease
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Car
import com.ivangarzab.carrus.ui.compose.BigNeutralButton
import com.ivangarzab.carrus.ui.compose.BigPositiveButton
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
            onUpdateState = {
                viewModel.onUpdateStateData(
                    nickname = it.nickname,
                    make = it.make,
                    model = it.model,
                    year = it.year,
                    licenseNo = it.licenseNo,
                    vinNo = it.vinNo,
                    tirePressure = it.tirePressure,
                    totalMiles = it.totalMiles,
                    milesPerGallon = it.milesPerGallon
                )
            },
            onBackPressed = { onBackPressed() },
            onImportClicked = { onImportClicked() },
            onAddImageClicked = { onAddImageClicked() },
            onDeleteImageClicked = { viewModel.onImageDeleted() },
            onActionButtonClicked = { make, model, year ->
                viewModel.verifyData(make, model, year)
            },
            addTestCarData = {
                if (isRelease().not()) {
                    viewModel.apply {
                        with(Car.default) {
                            onUpdateStateData(
                                nickname = nickname,
                                make = make,
                                model = model,
                                year = year,
                                licenseNo = licenseNo,
                                vinNo = vinNo,
                                tirePressure = tirePressure,
                                totalMiles = totalMiles,
                                milesPerGallon = milesPerGallon
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateScreen(
    @PreviewParameter(CarModalStatePreview::class) state: CarModalState,
    onUpdateState: (CarModalState) -> Unit = { },
    onBackPressed: () -> Unit = { },
    onImportClicked: () -> Unit = { },
    onAddImageClicked: () -> Unit = { },
    onDeleteImageClicked: () -> Unit = { },
    onActionButtonClicked: (String, String, String) -> Unit = { _, _, _ -> },
    // Easter egg
    addTestCarData: () -> Unit = { }
) {
    AppTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = state.title,
                    isNavigationIconEnabled = true,
                    onNavigationIconClicked = onBackPressed,
                    isActionIconEnabled = true,
                    onActionIconClicked = onImportClicked
                )
            }
        ) { paddingValues ->
            CreateScreenContent(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onUpdateState = onUpdateState,
                onAddImageClicked = onAddImageClicked,
                onDeleteImageClicked = onDeleteImageClicked,
                onActionButtonClicked = { p1, p2, p3 ->
                    onActionButtonClicked(p1, p2, p3)
                },
                addTestCarData = addTestCarData
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateScreenContent(
    modifier: Modifier = Modifier,
    @PreviewParameter(CarModalStatePreview::class) state: CarModalState,
    onUpdateState: (CarModalState) -> Unit = { },
    onAddImageClicked: () -> Unit = { },
    onDeleteImageClicked: () -> Unit = { },
    onUpdateAllData: () -> Unit = { },
    onActionButtonClicked: (String, String, String) -> Unit = { _, _, _ -> },
    //Easter egg
    addTestCarData: () -> Unit = { }
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
                    .verticalScroll(
                        state = rememberScrollState(),
                        enabled = true
                    )
            ) {
                if (state.imageUri == null) {
                    BigNeutralButton(
                        text = stringResource(id = R.string.add_a_photo),
                        onClick = { onAddImageClicked() }
                    )
                } else {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .combinedClickable(
                                    onClick = { },
                                    onLongClick = { addTestCarData() }
                                ),
                            painter = rememberAsyncImagePainter(
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(data = state.imageUri)
                                    .build()
                            ),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Car image"
                        )
                        IconButton(
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.BottomStart)
                                .background(
                                    color = colorResource(id = R.color.black_30_percent),
                                    shape = CircleShape
                                ),
                            onClick = onAddImageClicked
                        ) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                painter = painterResource(id = R.drawable.ic_image_upload),
                                contentDescription = "Add image icon button",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.BottomEnd)
                                .background(
                                    color = colorResource(id = R.color.black_30_percent),
                                    shape = CircleShape
                                ),
                            onClick = onDeleteImageClicked
                        ) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                painter = painterResource(id = R.drawable.ic_delete_trash),
                                contentDescription = "Delete image icon button",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation),
                    label = stringResource(id = R.string.make),
                    content = state.make,
                    isRequired = true,
                    updateListener = {
                        onUpdateState(state.copy(
                            make = it
                        ))
                    }
                )
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation),
                    label = stringResource(id = R.string.model),
                    content = state.model,
                    isRequired = true,
                    updateListener = {
                        onUpdateState(state.copy(
                            model = it
                        ))
                    }
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
                        content = state.year,
                        isRequired = true,
                        updateListener = {
                            onUpdateState(state.copy(
                                year = it
                            ))
                        }
                    )
                    CreateTextField(
                        modifier = Modifier
                            .padding(start = spaceInBetween)
                            .weight(3f),
                        label = stringResource(id = R.string.license_no),
                        content = state.licenseNo,
                        updateListener = {
                            onUpdateState(state.copy(
                                licenseNo = it
                            ))
                        }
                    )
                }
                CreateTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation),
                    label = stringResource(id = R.string.nickname),
                    content = state.nickname,
                    isLastField = isExpanded.not(),
                    updateListener = {
                        onUpdateState(state.copy(
                            nickname = it
                        ))
                    }
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
                            content = state.totalMiles,
                            updateListener = {
                                onUpdateState(state.copy(
                                    totalMiles = it
                                ))
                            }
                        )
                        CreateNumberField(
                            modifier = Modifier
                                .padding(start = spaceInBetween)
                                .weight(2f),
                            label = stringResource(id = R.string.miles_per_gallon),
                            content = state.milesPerGallon,
                            updateListener = {
                                onUpdateState(state.copy(
                                    milesPerGallon = it
                                ))
                            }
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
                            content = state.tirePressure,
                            updateListener = {
                                onUpdateState(state.copy(
                                    tirePressure = it
                                ))
                            }
                        )
                        CreateTextField(
                            modifier = Modifier
                                .padding(start = spaceInBetween)
                                .weight(3f),
                            label = stringResource(id = R.string.vin_no),
                            content = state.vinNo,
                            isLastField = true,
                            updateListener = {
                                onUpdateState(state.copy(
                                    vinNo = it
                                ))
                            }
                        )
                    }
                }

                BigNeutralButton(
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
                BigPositiveButton(
                    modifier = Modifier.padding(top = verticalSeparation),
                    text = state.actionButton,
                    onClick = { onActionButtonClicked(
                        state.make,
                        state.model,
                        state.year
                    ) }
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
private fun CreateTextField(
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
private fun CreateNumberField(
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