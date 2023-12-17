package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.data.repositories.AlarmsRepository
import com.ivangarzab.carrus.data.repositories.AlarmsRepositoryImpl
import io.mockk.mockk
import org.junit.Test

class AlarmsRepositoryModuleTest {

    private val repository: AlarmsRepositoryImpl = mockk()

    private val module = object : AlarmsRepositoryModule() {
        override fun bindAlarmsRepository(
            alarmsRepository: AlarmsRepositoryImpl
        ): AlarmsRepository = repository
    }

    @Test
    fun test_binding() {
        val result = module.bindAlarmsRepository(repository)
        assertThat(result)
            .isNotNull()
    }
}