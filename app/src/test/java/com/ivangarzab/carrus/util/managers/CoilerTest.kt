package com.ivangarzab.carrus.util.managers

import io.mockk.mockk
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class CoilerTest {

    private lateinit var coiler: Coiler

    @Before
    fun setup() {
        coiler = Coiler()
    }

    @Test
    fun test_setImageLoader() {
        coiler.setImageLoader(mockk(relaxUnitFun = true))
    }
}