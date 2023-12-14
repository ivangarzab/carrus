package com.ivangarzab.carrus.util.receivers

import android.app.AlarmManager
import android.content.Intent
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AlarmPermissionStateChangedReceiverTest {

    private var listenedState: Boolean? = null

    private val receiver: AlarmPermissionStateChangedReceiver = AlarmPermissionStateChangedReceiver {
        listenedState = it
    }.apply {
        analytics = mockk(relaxed = true)
    }

    private lateinit var mockIntent: Intent

    @Before
    fun setup() {
        listenedState = null
        mockIntent = mockk(relaxed = true)
    }

    @Test
    fun test_onReceive_null_intent() {
        receiver.onReceive(mockk(relaxed = true), null)
        assertThat(listenedState)
            .isNull()
    }

    @Test
    fun test_onReceive_correct_intent() {
        every { mockIntent.action } returns AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
        receiver.onReceive(mockk(relaxed = true), mockIntent)
        assertThat(listenedState)
            .isTrue()
    }

    @Test
    fun test_onReceive_incorrect_intent() {
        every { mockIntent.action } returns Intent.ACTION_BATTERY_CHANGED
        receiver.onReceive(mockk(relaxed = true), mockIntent)
        assertThat(listenedState)
            .isNull()
    }
}