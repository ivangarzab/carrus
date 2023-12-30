package com.ivangarzab.carrus.util.helpers

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ivangarzab.test_data.STRING_BLANK
import com.ivangarzab.test_data.STRING_EMPTY
import com.ivangarzab.test_data.STRING_TEST
import com.ivangarzab.test_data.data.URI_TEST
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Ivan Garza Bermea.
 */
@RunWith(AndroidJUnit4::class)
class ContentResolverHelperTest {

    private lateinit var helper: ContentResolverHelper

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

    @Test
    fun test_writeInFile_valid_uri_valid_content() {
        val result = helper.writeInFile(mockk(), STRING_TEST)
        assertThat(result)
            .isTrue()
    }

    @Test
    fun test_writeInFile_valid_uri_empty_content() {
        val result = helper.writeInFile(mockk(), STRING_EMPTY)
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_writeInFile_valid_uri_blank_content() {
        val result = helper.writeInFile(mockk(), STRING_BLANK)
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_writeInFile_invalid_uri_valid_content() {
        val result = helper.writeInFile(Uri.EMPTY, STRING_TEST)
        assertThat(result)
            .isFalse()
    }

    @Test
    fun test_readFromFile_valid_uri() {
        val result = helper.readFromFile(URI_TEST)
        assertThat(result)
            .isEqualTo("success")
    }

    @Test
    fun test_readFromFile_invalid_uri() {
        val result = helper.readFromFile(Uri.EMPTY)
        assertThat(result)
            .isNull()
    }

    companion object {
        private const val TEST_URI_STR = "test-uri"
        private val TEST_URI = Uri.parse("test-uri")
    }
}