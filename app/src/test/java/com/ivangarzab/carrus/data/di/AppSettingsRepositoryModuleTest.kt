package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.repositories.AppSettingsRepository
import com.ivangarzab.carrus.data.repositories.AppSettingsRepositoryImpl
import io.mockk.mockk
import org.junit.Test

class AppSettingsRepositoryModuleTest {

    private val repository: AppSettingsRepositoryImpl = mockk()

    private val module = object : AppSettingsRepositoryModule() {
        override fun bindAppSettingsRepository(
            appSettingsRepository: AppSettingsRepositoryImpl
        ): AppSettingsRepository = repository
    }

    @Test
    fun test_binding() {
        val result = module.bindAppSettingsRepository(repository)
        assertThat(result)
            .isNotNull()
    }
}