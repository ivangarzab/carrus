package com.ivangarzab.carrus.ui.create

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.BigNeutralButton
import com.ivangarzab.carrus.ui.compose.BigPositiveButton
import com.ivangarzab.carrus.ui.compose.NavigationBottomBar
import com.ivangarzab.carrus.ui.compose.NumberInputField
import com.ivangarzab.carrus.ui.compose.TextInputField
import com.ivangarzab.carrus.ui.compose.TopBar
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.create.data.CarModalState
import com.ivangarzab.carrus.ui.create.data.CarModalStatePreview
import org.koin.androidx.compose.koinViewModel

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun CreateScreenStateful(
    viewModel: CreateViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onNavSettingsPressed: () -> Unit,
    onNavHomePressed: () -> Unit,
    onNavMapPressed: () -> Unit,
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
                    licenseState = it.licenseState,
                    licenseNo = it.licenseNo,
                    vinNo = it.vinNo,
                    tirePressure = it.tirePressure,
                    totalMiles = it.totalMiles,
                    milesPerGalCity = it.milesPerGalCity,
                    milesPerGalHighway = it.milesPerGalHighway
                )
            },
            onBackPressed = { onBackPressed() },
            onNavigateHomePressed = onNavHomePressed,
            onNavigateSettingsPressed = onNavSettingsPressed,
            onNavigateMapPressed = onNavMapPressed,
            onImportClicked = { onImportClicked() },
            onAddImageClicked = { onAddImageClicked() },
            onDeleteImageClicked = { viewModel.onImageDeleted() },
            onActionButtonClicked = { make, model, year ->
                viewModel.verifyData(make, model, year)
            },
            addTestCarData = { viewModel.setupDataEasterEggForTesting() }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateScreen(
    @PreviewParameter(CarModalStatePreview::class) state: CarModalState,
    onUpdateState: (CarModalState) -> Unit = { },
    onBackPressed: () -> Unit = { },
    onNavigateHomePressed: () -> Unit = { },
    onNavigateSettingsPressed: () -> Unit = { },
    onNavigateMapPressed: () -> Unit = { },
    onImportClicked: () -> Unit = { },
    onAddImageClicked: () -> Unit = { },
    onDeleteImageClicked: () -> Unit = { },
    onActionButtonClicked: (String, String, String) -> Unit = { _, _, _ -> },
    // Easter egg
    addTestCarData: () -> Unit = { }
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.statusBarDarkContentEnabled = false

    AppTheme {
        Scaffold(
            topBar = {
                TopBar(
                    modifier = Modifier.combinedClickable(
                        onClick = { },
                        onLongClick = { addTestCarData() }
                    ),
                    title = state.title,
                    isNavigationIconEnabled = true,
                    onNavigationIconClicked = onBackPressed,
                    isActionIconEnabled = true,
                    action = {
                        IconButton(
                            onClick = { onImportClicked() }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_import),
                                contentDescription = stringResource(id = R.string.label_icon),
                                tint = if (isSystemInDarkTheme()) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                }
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBottomBar(
                    settingsButtonClicked = onNavigateSettingsPressed,
                    homeButtonClicked = onNavigateHomePressed,
                    mapButtonClicked = onNavigateMapPressed
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
                }
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
    onUpdateState: (CarModalState) -> Unit = { },
    onAddImageClicked: () -> Unit = { },
    onDeleteImageClicked: () -> Unit = { },
    onActionButtonClicked: (String, String, String) -> Unit = { _, _, _ -> },
) {
    fun shouldBeExpanded(): Boolean = state.let {
        it.totalMiles.isNotBlank() ||
                it.milesPerGalCity.isNotBlank() ||
                it.milesPerGalHighway.isNotBlank() ||
                it.vinNo.isNotBlank() ||
                it.tirePressure.isNotBlank()
    }

    val verticalSeparation: Dp = 12.dp
    val spaceInBetween: Dp = 8.dp
    var isExpanded: Boolean by rememberSaveable {
        mutableStateOf(shouldBeExpanded())
    }
    var isImagePresent: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    isImagePresent = state.imageUri != null

    AppTheme {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                    .verticalScroll(
                        state = rememberScrollState(),
                        enabled = true
                    )
            ) {
                AnimatedVisibility(
                    visible = isImagePresent.not(),
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    BigNeutralButton(
                        text = stringResource(id = R.string.add_a_photo),
                        onClick = { onAddImageClicked() }
                    )
                }
                AnimatedVisibility(
                    visible = isImagePresent,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    val imageContainerShape: Shape = RoundedCornerShape(4.dp)
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(
                            color = MaterialTheme.colorScheme.primary,
                            shape = imageContainerShape,
                            width = 1.dp
                        )
                        .clipToBounds()
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(imageContainerShape),
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
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_image_add),
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
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_trash),
                                contentDescription = "Delete image icon button",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                TextInputField(
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
                TextInputField(
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
                    NumberInputField(
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
                    TextInputField(
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = verticalSeparation)
                ) {
                    TextInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f)
                            .padding(end = spaceInBetween),
                        label = stringResource(id = R.string.nickname),
                        content = state.nickname,
                        updateListener = {
                            onUpdateState(state.copy(
                                nickname = it
                            ))
                        }
                    )
                    TextInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(start = spaceInBetween),
                        label = stringResource(id = R.string.state),
                        content = state.licenseState,
                        isLastField = isExpanded.not(),
                        updateListener = {
                            onUpdateState(state.copy(
                                licenseState = it
                            ))
                        }
                    )
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = verticalSeparation)
                        ) {
                            NumberInputField(
                                modifier = Modifier
                                    .padding(end = spaceInBetween)
                                    .weight(3f),
                                label = stringResource(id = R.string.total_miles),
                                content = state.totalMiles,
                                updateListener = {
                                    onUpdateState(
                                        state.copy(
                                            totalMiles = it
                                        )
                                    )
                                }
                            )
                            NumberInputField(
                                modifier = Modifier
                                    .padding(start = spaceInBetween)
                                    .weight(2f),
                                label = stringResource(id = R.string.tire_pressure),
                                content = state.tirePressure,
                                updateListener = {
                                    onUpdateState(
                                        state.copy(
                                            tirePressure = it
                                        )
                                    )
                                }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = verticalSeparation)
                        ) {
                            NumberInputField(
                                modifier = Modifier
                                    .padding(end = spaceInBetween)
                                    .weight(2f),
                                label = stringResource(id = R.string.mi_per_gal_city),
                                content = state.milesPerGalCity,
                                updateListener = {
                                    onUpdateState(
                                        state.copy(
                                            milesPerGalCity = it
                                        )
                                    )
                                }
                            )
                            NumberInputField(
                                modifier = Modifier
                                    .padding(start = spaceInBetween)
                                    .weight(2f),
                                label = stringResource(id = R.string.mi_per_gal_highway),
                                content = state.milesPerGalHighway,
                                updateListener = {
                                    onUpdateState(
                                        state.copy(
                                            milesPerGalHighway = it
                                        )
                                    )
                                }
                            )
                        }
                        TextInputField(
                            modifier = Modifier
                                .padding(top = verticalSeparation)
                                .fillMaxWidth(),
                            label = stringResource(id = R.string.vin_no),
                            content = state.vinNo,
                            isLastField = true,
                            updateListener = {
                                onUpdateState(
                                    state.copy(
                                        vinNo = it
                                    )
                                )
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
                    modifier = Modifier.padding(top = verticalSeparation, bottom = verticalSeparation),
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