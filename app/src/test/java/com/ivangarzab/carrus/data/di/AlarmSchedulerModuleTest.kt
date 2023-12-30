package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.util.managers.AlarmScheduler
import com.ivangarzab.carrus.util.managers.AlarmSchedulerImpl
import io.mockk.mockk
import org.junit.Test

class AlarmSchedulerModuleTest {

    private val scheduler: AlarmSchedulerImpl = mockk()

    private val module = object : AlarmSchedulerModule() {
        override fun bindAlarmScheduler(
            alarmScheduler: AlarmSchedulerImpl
        ): AlarmScheduler = scheduler

    }

    @Test
    fun test_binding() {
        val result = module.bindAlarmScheduler(scheduler)
        assertThat(result)
            .isNotNull()
    }
}