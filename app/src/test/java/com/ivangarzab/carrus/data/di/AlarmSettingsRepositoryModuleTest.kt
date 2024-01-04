package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepository
import com.ivangarzab.carrus.data.repositories.AlarmSettingsRepositoryImpl
import io.mockk.mockk
import org.junit.Test

class AlarmSettingsRepositoryModuleTest {

    private val repository: AlarmSettingsRepositoryImpl = mockk()

    private val module = object : AlarmSettingsRepositoryModule() {
        override fun bindAlarmSettingsRepository(
            alarmSettingsRepository: AlarmSettingsRepositoryImpl
        ): AlarmSettingsRepository = repository
    }

    @Test
    fun test_binding() {
        val result = module.bindAlarmSettingsRepository(repository)
        assertThat(result)
            .isNotNull()
    }
}