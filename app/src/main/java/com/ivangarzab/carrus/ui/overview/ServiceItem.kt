package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.Service
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */

@Composable
fun OverviewServiceItemStateful(
    modifier: Modifier = Modifier,
    index: Int = -1,
    data: Service = Service.empty,
    onEditClicked: () -> Unit = { },
    onDeleteClicked: () -> Unit = { }
) {
    var isExpanded: Boolean = rememberSaveable { false }

    AppTheme {
        OverviewServiceItem(modifier, data, isExpanded, onEditClicked, onDeleteClicked) {
            isExpanded = isExpanded.not()
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OverviewServiceItem(
    modifier: Modifier = Modifier,
    data: Service = Service.empty,
    isExpanded: Boolean = true,
    onEditClicked: () -> Unit = { },
    onDeleteClicked: () -> Unit = { },
    onExpandClicked: () -> Unit = { }
) {
    AppTheme {
        Card(
            modifier = modifier
                .padding(top = 6.dp, bottom = 6.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(modifier = modifier
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
            ) {
                Row {
                    Text(
                        modifier = Modifier
                            .weight(7f)
                            .align(Alignment.CenterVertically),
                        text = "Service name",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        modifier = Modifier
                            .weight(2f)
                            .align(Alignment.CenterVertically),
                        text = "DUE date",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = onExpandClicked
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown arrow icon"
                        )
                    }
                }
                if (isExpanded) {
                    Row {
                        Text(
                            modifier = Modifier.weight(7f),
                            text = "Service details",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        val costAndDateTextStyle: TextStyle = MaterialTheme.typography.bodyMedium
                        Column(modifier = Modifier.weight(3f)) {
                            Text(
                                modifier = Modifier.align(Alignment.End),
                                text = "$0.00",
                                style = costAndDateTextStyle
                            )
                            Text(
                                modifier = Modifier.align(Alignment.End),
                                text = "on mm/DD/YY",
                                style = costAndDateTextStyle
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = onEditClicked
                        ) {
                            Icon(
                                modifier = Modifier.padding(6.dp),
                                painter = painterResource(
                                    id = R.drawable.ic_edit
                                ),
                                contentDescription = "Edit icon button"
                            )
                        }
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = onDeleteClicked
                        ) {
                            Icon(
                                modifier = Modifier.padding(6.dp),
                                painter = painterResource(
                                    id = R.drawable.ic_delete_trash
                                ),
                                contentDescription = "Edit icon button"
                            )
                        }
                    }
                }
            }
        }
    }
}