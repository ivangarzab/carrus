package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
    state: DetailsPanelState = DetailsPanelState(),
    onEditCarClicked: () -> Unit,
) {
    val totalValidFields = state.getTotalValidFields()
    val gridSize: Dp = if (totalValidFields > 3) {
        200.dp
    } else if (totalValidFields in 1..3) {
        100.dp
    } else {
        0.dp
    }
    AppTheme {
        Column(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp)
            ) {
                PanelTitleText(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(id = R.string.details)
                )
                PanelIcon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    onClick = onEditCarClicked,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = "Car edit icon button"
                )
            }
            OverviewScreenDetailsPanelGrid(
                modifier = Modifier
                    .height(gridSize),
                rows = if (state.getTotalValidFields() > 3) 2 else 1,
                state = state
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreenDetailsPanelGrid(
    modifier: Modifier = Modifier,
    rows: Int = 1,
    state: DetailsPanelState = DetailsPanelState()
) {
    AppTheme {
        LazyHorizontalStaggeredGrid(
            modifier = modifier,
            rows = StaggeredGridCells.Fixed(count = rows),
            contentPadding = PaddingValues(4.dp)
        ) {
            state.year.takeIf { it.isNotEmpty() }?.let {
                item {
                    OverviewScreenDetailsItem(title = "Year", content = state.year)
                }
            }
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
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Box(modifier = Modifier) {
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
            modifier = Modifier,
            state = DetailsPanelState(
                year = "2099",
                licenseState = "Texas",
                tirePressure = "32",
                totalMiles = "99999",
                milesPerGalHighway = "32",
                milesPerGalCity = "26"
            ),
            onEditCarClicked = { }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun OverviewScreenDetailsItemPreview() {
    OverviewScreenDetailsItem(
        title = "State",
        content = "Texas"
    )
}
