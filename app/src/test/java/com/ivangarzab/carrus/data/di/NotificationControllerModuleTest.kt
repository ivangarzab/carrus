package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.util.managers.NotificationController
import com.ivangarzab.carrus.util.managers.NotificationControllerImpl
import io.mockk.mockk
import org.junit.Test

class NotificationControllerModuleTest {

    private val controller: NotificationControllerImpl = mockk()

    private val module = object : NotificationControllerModule() {
        override fun bindNotificationController(
            notificationController: NotificationControllerImpl
        ): NotificationController = controller
    }

    @Test
    fun test_binding() {
        val result = module.bindNotificationController(controller)
        assertThat(result)
            .isNotNull()
    }
}