package com.ivangarzab.carrus

import com.ivangarzab.carrus.util.managers.Coiler
import com.ivangarzab.carrus.util.managers.LeakUploader
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Created by Ivan Garza Bermea.
 */
@RunWith(RobolectricTestRunner::class)
class AppTest {

    private lateinit var app: App

    private lateinit var mockLeakUploader: LeakUploader

    private lateinit var mockCoiler: Coiler

    @Before
    fun setup() {
        mockLeakUploader = spyk()
        mockCoiler = spyk()
        app = TestApp(lu = mockLeakUploader, c = mockCoiler)
    }

    @Test
    fun test_setupCoil() {
        app.setupCoil()
        verify { mockCoiler.setImageLoader(any()) }
    }

    @Test
    fun test_setupLeakCanary() {
        app.setupLeakCanary()
        verify { mockLeakUploader.setupCrashlyticsLeakUploader() }
    }

    @Test
    fun test_onCreate() {
        app.onCreate()
        verify { mockCoiler.setImageLoader(any()) }
        verify { mockLeakUploader.setupCrashlyticsLeakUploader() }
    }
}