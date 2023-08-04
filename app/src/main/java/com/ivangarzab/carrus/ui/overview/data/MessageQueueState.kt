package com.ivangarzab.carrus.ui.overview.data

import android.os.Parcelable
import com.ivangarzab.carrus.util.managers.UniqueMessageQueue
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class MessageQueueState(
    val messageQueue: UniqueMessageQueue = UniqueMessageQueue()
) : Parcelable
