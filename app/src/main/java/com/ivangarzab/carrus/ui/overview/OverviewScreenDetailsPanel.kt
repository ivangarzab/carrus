package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.ui.compose.theme.AppTheme
import com.ivangarzab.carrus.ui.overview.data.DetailsPanelState

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreenDetailsPanel(
    modifier: Modifier = Modifier,
    state: DetailsPanelState = DetailsPanelState()
) {
    AppTheme {
        LazyHorizontalStaggeredGrid(
            modifier = modifier,
            rows = StaggeredGridCells.Fixed(count = 2),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                OverviewScreenDetailsItem(title = "License State", content = state.licenseState)
            }
            item {
                OverviewScreenDetailsItem(title = "License Plate", content = state.licenseNo)
            }
            item {
                OverviewScreenDetailsItem(title = "Vin Number", content = state.vinNo)
            }
            item {
                OverviewScreenDetailsItem(title = "Tire Pressure", content = state.tirePressure)
            }
            item {
                OverviewScreenDetailsItem(title = "Total Miles", content = state.totalMiles)
            }
            item {
                OverviewScreenDetailsItem(title = "City mi/gal", content = state.milesPerGalCity)
            }
            item {
                OverviewScreenDetailsItem(title = "Highway mi/gal", content = state.milesPerGalHighway)
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
        ElevatedCard(
            modifier = Modifier
                .padding(8.dp)
                .clipToBounds()
                .clickable { /* TODO: Call the VM, if needed */ }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
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
            modifier = Modifier.height(200.dp),
            DetailsPanelState(
                licenseState = "TX",
                licenseNo = "DH9 L474",
                vinNo = "ABCDEFGHIJKLMNOPQ",
                tirePressure = "32",
                totalMiles = "100,000",
                milesPerGalCity = "23",
                milesPerGalHighway = "30"
            )
        )
    }
}
