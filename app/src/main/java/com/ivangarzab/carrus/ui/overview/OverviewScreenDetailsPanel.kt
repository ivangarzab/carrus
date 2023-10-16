package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.ui.compose.PanelIcon
import com.ivangarzab.carrus.ui.compose.PanelTitleText
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState
import com.ivangarzab.carrus.util.extensions.parseIntoNumberWithCommas

/**
 * Created by Ivan Garza Bermea.
 */
@Composable
fun OverviewScreenDetailsPanel(
    modifier: Modifier = Modifier,
    gridSize: Dp = 200.dp,
    state: DetailsPanelState = DetailsPanelState(),
    onEditCarClicked: () -> Unit,
) {
    AppTheme {
        Column(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                PanelTitleText(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(id = R.string.details)
                )
                PanelIcon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onEditCarClicked,
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Car edit icon button"
                )
            }
            OverviewScreenDetailsPanelGrid(
                modifier = Modifier
                    .height(gridSize),
                state = state
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreenDetailsPanelGrid(
    modifier: Modifier = Modifier,
    state: DetailsPanelState = DetailsPanelState()
) {
    AppTheme {
        LazyHorizontalStaggeredGrid(
            modifier = modifier
                .padding(end = 16.dp),
            rows = StaggeredGridCells.Fixed(count = 2),
            contentPadding = PaddingValues(4.dp)
        ) {
            state.licenseState.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(title = "License State", content = state.licenseState)
                }
            }
            state.licenseNo.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(title = "License Plate", content = state.licenseNo)
                }
            }
            state.tirePressure.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(title = "Tire Pressure", content = state.tirePressure)
                }
            }
            state.totalMiles.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(
                        title = "Total Miles",
                        content = state.totalMiles.parseIntoNumberWithCommas()
                    )
                }
            }
            state.milesPerGalCity.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(
                        title = "City mi/gal",
                        content = state.milesPerGalCity
                    )
                }
            }
            state.milesPerGalHighway.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(
                        title = "Highway mi/gal",
                        content = state.milesPerGalHighway
                    )
                }
            }
            state.vinNo.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(title = "Vin Number", content = state.vinNo)
                }
            }
        }
    }
}

@Composable
fun OverviewScreenDetailsItem(
    title: String,
    content: String
) {
    AppTheme {
        Card(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold //TODO: Make a diff color instead
                )
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        text = content,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun OverviewScreenDetailsPanelPreview() {
    AppTheme {
        OverviewScreenDetailsPanel(
            modifier = Modifier.height(250.dp),
            state = DetailsPanelState(
                licenseState = "",
                licenseNo = "DH9 L474",
                vinNo = "ABCDEFGHIJKLMNOPQ",
                tirePressure = "32",
                totalMiles = "1000000",
                milesPerGalCity = "23",
                milesPerGalHighway = "30"
            ),
            onEditCarClicked = { }
        )
    }
}
