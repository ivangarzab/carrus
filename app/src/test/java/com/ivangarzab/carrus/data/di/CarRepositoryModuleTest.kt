package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth
import com.ivangarzab.carrus.data.repositories.CarRepository
import com.ivangarzab.carrus.data.repositories.CarRepositoryImpl
import io.mockk.mockk
import org.junit.Test

class CarRepositoryModuleTest {

    private val repository: CarRepositoryImpl = mockk()

    private val module = object : CarRepositoryModule() {
        override fun bindCarRepository(
            carRepository: CarRepositoryImpl
        ): CarRepository = repository

    }

    @Test
    fun test_binding() {
        val result = module.bindCarRepository(repository)
        Truth.assertThat(result)
            .isNotNull()
    }
}