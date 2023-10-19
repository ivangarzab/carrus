package com.ivangarzab.carrus.ui.overview.data

import android.os.Parcelable
import com.ivangarzab.carrus.data.structures.UniqueMessageQueue
import kotlinx.parcelize.Parcelize

/**
 * Created by Ivan Garza Bermea.
 */
@Parcelize
data class MessageQueueState(
    val messageQueue: UniqueMessageQueue = UniqueMessageQueue()
) : Parcelable
