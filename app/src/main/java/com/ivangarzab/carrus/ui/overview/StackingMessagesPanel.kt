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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
                    .height(115.dp)
            ) {
                ConstraintLayout {
                    val message: ConstrainedLayoutReference = createRef()
                    val badge: ConstrainedLayoutReference = createRef()
                    if (messageQueue.isNotEmpty()) {
                        MessageItem(
                            modifier = Modifier
                                .constrainAs(message) { }
                                .padding(4.dp),
                            message = messageQueue.get().text,
                            iconRes = messageQueue.get().iconRes,
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
    message: String,
    iconRes: Int,
    onDeleteButtonClicked: () -> Unit,
    onContentClicked: () -> Unit
) {
    AppTheme {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 115.dp)
                .clip(CardDefaults.shape)
                .clickable { onContentClicked() }
        ) {
            ConstraintLayout {
                val bg: ConstrainedLayoutReference = createRef()
                val icon: ConstrainedLayoutReference = createRef()
                val text: ConstrainedLayoutReference = createRef()
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .constrainAs(bg) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                        .fadingEdge(
                            Brush.linearGradient(
                                0.00f to Color.Black.copy(
                                    alpha = 0.40f
                                ),
                                0.20f to Color.Black.copy(
                                    alpha = 0.30f
                                ),
                                0.30f to Color.Black.copy(
                                    alpha = 0.20f
                                ),
                                0.75f to Color.Black.copy(
                                    alpha = 0.10f
                                ),
                                1.00f to Color.Transparent
                            )
                        ),
                    imageVector = ImageVector.vectorResource(id = iconRes),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                    contentDescription = "Background image"
                )

                IconButton(
                    modifier = Modifier.constrainAs(icon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                    onClick = onDeleteButtonClicked
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Delete message button"
                    )
                }
                val textPadding: Dp = 16.dp
                Text(
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(icon.bottom)
                        }
                        .padding(
                            start = textPadding,
                            end = textPadding,
                            bottom = textPadding
                        ),
                    text = message,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
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
        message = "Derive intentions ocean ubermensch prejudice. Pious transvaluation society" +
                " justice sea evil convictions sea insofar madness fearful Zarathustra.",
        iconRes = R.drawable.ic_mark_i,
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
            label = ""
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