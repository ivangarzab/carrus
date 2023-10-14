package com.ivangarzab.carrus.util.helpers

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.carrus.STRING_EMPTY
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Ivan Garza Bermea.
 */
@RunWith(AndroidJUnit4::class)
class ContentResolverUtilTest {

    private lateinit var helper: TestContentResolverHelper

    @Before
    fun setup() {
        helper = TestContentResolverHelper()
    }

    @Test
    fun test_persistUriPermission_empty_uri_failure() {
        val result = helper.persistUriPermission(Uri.EMPTY)
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_persistUriPermission_uri_success() {
        assertThat(helper.persistUriPermission(TEST_URI))
            .isTrue()
    }

    @Test
    fun test_persistUriPermission_empty_string_failure() {
        val result = helper.persistUriPermission(STRING_EMPTY)
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_persistUriPermission_string_success() {
        val result = helper.persistUriPermission(TEST_URI_STR)
        assertThat(result)
            .isTrue()
    }

    companion object {
        private const val TEST_URI_STR = "test-uri"
        private val TEST_URI = Uri.parse("test-uri")
    }
}