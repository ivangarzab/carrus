package com.ivangarzab.carrus.ui.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.ivangarzab.carrus.R
import com.ivangarzab.carrus.data.models.Message
import com.ivangarzab.carrus.data.models.MessageData
import com.ivangarzab.carrus.data.structures.MessageQueue
import com.ivangarzab.carrus.ui.compose.fadingEdge
import com.ivangarzab.carrus.ui.compose.theme.AppTheme

/**
 * Created by Ivan Garza Bermea.
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StackingMessagesPanel(
    modifier: Modifier = Modifier,
    messageQueue: MessageQueue = MessageQueue().apply {
        add(Message.TEST.data)
        add(Message.MISSING_PERMISSION_NOTIFICATION.data)
    },
    onDismissClicked: () -> Unit = { },
    onMessageClicked: (String) -> Unit = { }
) {
    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    visible = messageQueue.isNotEmpty()

    AppTheme {
        AnimatedVisibility(
            visible = visible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 115.dp)
            ) {
                ConstraintLayout {
                    val message: ConstrainedLayoutReference = createRef()
                    val badge: ConstrainedLayoutReference = createRef()
                    if (messageQueue.isNotEmpty()) {
                        MessageItem(
                            modifier = Modifier
                                .constrainAs(message) { }
                                .padding(4.dp),
                            data = messageQueue.get(),
                            onDeleteButtonClicked = {
                                onDismissClicked()
                            },
                            onContentClicked = { onMessageClicked(messageQueue.get().id) }
                        )
                        if (messageQueue.size() > 1) {
                            StackingMessagesBadge(
                                modifier = Modifier
                                    .constrainAs(badge) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    }
                                    .animateEnterExit(
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    ),
                                text = messageQueue.size().toString()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    data: MessageData,
    onDeleteButtonClicked: () -> Unit,
    onContentClicked: () -> Unit
) {
    AppTheme {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp)
                .clip(CardDefaults.shape)
                .clickable { onContentClicked() }
        ) {
            ConstraintLayout {
                val bg: ConstrainedLayoutReference = createRef()
                val icon: ConstrainedLayoutReference = createRef()
                val body: ConstrainedLayoutReference = createRef()
                val title: ConstrainedLayoutReference = createRef()
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .constrainAs(bg) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fadingEdge(
                            Brush.linearGradient(
                                0.00f to Color.Transparent,
                                0.25f to Color.Black.copy(
                                    alpha = 0.10f
                                ),
                                0.70f to Color.Black.copy(
                                    alpha = 0.20f
                                ),
                                0.85f to Color.Black.copy(
                                    alpha = 0.30f
                                ),
                                1.00f to Color.Black.copy(
                                    alpha = 0.40f
                                )
                            )
                        ),
                    imageVector = ImageVector.vectorResource(id = data.iconRes),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inversePrimary),
                    contentDescription = "Message background image"
                )

                IconButton(
                    modifier = Modifier.constrainAs(icon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                    onClick = onDeleteButtonClicked
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                        contentDescription = "Delete message button"
                    )
                }
                val textPadding: Dp = 16.dp
                Text(
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .padding(
                            top = textPadding + 8.dp,
                            start = textPadding,
                            end = 32.dp
                        ),
                    text = data.title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .constrainAs(body) {
                            top.linkTo(icon.bottom)
                        }
                        .padding(
                            start = textPadding,
                            end = textPadding,
                            bottom = textPadding
                        ),
                    text = data.body,
                    fontStyle = FontStyle.Italic,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MessageItemPreview() {
    MessageItem(
        modifier = Modifier,
        data = Message.MISSING_PERMISSION_NOTIFICATION.data,
        onDeleteButtonClicked = { },
        onContentClicked = { }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StackingMessagesBadge(
    modifier: Modifier = Modifier,
    text: String = "6"
) {
    var value by remember {
        mutableStateOf(text)
    }
    value = text
    AppTheme {
        AnimatedContent(
            modifier = modifier,
            targetState = value,
            label = "Message count = $value"
        ) { targetValue ->
            Text(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                text = targetValue,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}