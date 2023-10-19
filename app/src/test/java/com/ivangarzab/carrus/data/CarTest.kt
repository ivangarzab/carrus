package com.ivangarzab.carrus.data

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.ivangarzab.carrus.data.models.Car
import com.ivangarzab.carrus.data.models.Service
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CarTest {

    private lateinit var defaultCar: Car
    private lateinit var emptyCar: Car

    @Before
    fun setUp() {
        defaultCar = Car.default
        emptyCar = Car.empty
    }

    @Test
    fun testToJson() {
        val defaultCarJson = defaultCar.toJson()
        val gson = Gson()
        val expectedJson = gson.toJson(defaultCar)

        assertEquals(expectedJson, defaultCarJson)
    }

    @Test
    fun testToStringVerbose() {
        val expectedToString = "Car(" +
                "\nnickname='Shaq'" +
                "\nmake='Chevrolet'" +
                "\nmodel='Malibu'" +
                "\nyear='2006'" +
                "\nlicenseNo='IGB066'" +
                "\nvinNo='4Y1SL65848Z411439'" +
                "\ntirePressure='35'" +
                "\ntotalMiles='99,999'" +
                "\nmi/gal='26'" +
                "\nservices='${Service.serviceList}'" +
                "\nimageUri=null" +
                "\n)"
        val carToString = defaultCar.toStringVerbose()

        assertEquals(expectedToString, carToString)
    }

    @Test
    fun testToString() = with(defaultCar) {
        val expectedToString = "Car ${nickname.ifBlank { "$make $model" }} with ${services.size} services"
        val carToString = defaultCar.toString()

        assertEquals(expectedToString, carToString)
    }

    @Test
    fun testCompareTo_failure() {
        val anotherCar = Car(
            uid = "456",
            nickname = "LeBron",
            make = "Chevrolet",
            model = "Impala",
            year = "2010",
            licenseNo = "XYZ123",
            vinNo = "5Y1SL67890Z123456",
            tirePressure = "32",
            totalMiles = "80,000",
            milesPerGallon = "22",
            services = Service.serviceList
        )

        assertThat(defaultCar.compareTo(anotherCar))
            .isNotEqualTo(COMPARE_ANSWER_EQUALS)
    }

    @Test
    fun testCompareTo_success() {
        val anotherCar = Car(
            uid = "123",
            nickname = "Shaq",
            make = "Chevrolet",
            model = "Malibu",
            year = "2006",
            licenseNo = "IGB066",
            vinNo = "4Y1SL65848Z411439",
            tirePressure = "35",
            totalMiles = "99,999",
            milesPerGallon = "26",
            services = Service.serviceList
        )

        val comparisonResult = defaultCar.compareTo(anotherCar)

        assertEquals(COMPARE_ANSWER_EQUALS, comparisonResult)
    }

    companion object {
        private const val COMPARE_ANSWER_EQUALS = 0
    }
}