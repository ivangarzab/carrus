package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
 @Composable
fun OverviewScreen() {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    AppTheme {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                OverviewScreenTopBar(scrollBehavior)
            },
            content = { paddingValues ->
                OverviewScreenContent(
                    modifier = Modifier.padding(paddingValues)
                )
            },
            bottomBar = {
                OverviewScreenBottomBar()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) {
    AppTheme {
        LargeTopAppBar(
            title = { Text("Test Top App Bar Title") },
            actions = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = ""
                )
            },
            scrollBehavior = scrollBehavior
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenBottomBar() {
    AppTheme {
        BottomAppBar(
            modifier = Modifier,
            actions = { },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {  }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add Service floating action button"
                    )
                }
            }
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewScreenContent(
    modifier: Modifier = Modifier,
    onSortRequest: (SortingCallback.SortingType) -> Unit = { },
    serviceList: List<Service> = Service.serviceList
) {
    AppTheme {
        LazyColumn(modifier = modifier) {
            item {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.services),
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            item {
                Row(modifier = Modifier
                    .padding(bottom = 8.dp),
                ) {
                    SortingButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onSortRequest(SortingCallback.SortingType.NONE )},
                        text = stringResource(id = R.string.none)
                    )
                    SortingButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onSortRequest(SortingCallback.SortingType.NAME )},
                        text = stringResource(id = R.string.name)
                    )
                    SortingButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onSortRequest(SortingCallback.SortingType.DATE )},
                        text = stringResource(id = R.string.date)
                    )
                }
            }
            if (serviceList.isNotEmpty()) {
                itemsIndexed(serviceList) { index, item ->
                    OverviewServiceItemStateful(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp),
                        index,
                        serviceList[index],
                        onEditClicked = { },
                        onDeleteClicked = { }
                    )
                }
            } else {
                item {
                    EmptyListView()
                }
            }
        }
    }
}

@Composable
private fun EmptyListView(
    modifier: Modifier = Modifier
) {
    AppTheme {
        Box(modifier = modifier
            .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.no_services_scheduled),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun SortingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    text: String = "button"
) {
    AppTheme {
        TextButton(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                ),
            onClick = onClick,
        ) {
            Text(
                text = text,
                fontStyle = FontStyle.Italic
            )
        }
    }
}