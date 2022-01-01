package com.ivangarzab.carbud.overview

import androidx.lifecycle.ViewModel
import com.ivangarzab.carbud.repositories.CarRepository

/**
 * Created by Ivan Garza Bermea.
 */
class OverviewViewModel : ViewModel() {

    private val carRepository = CarRepository()

    fun hasDefaultCar(): Boolean = carRepository.getDefaultCar() == null
}