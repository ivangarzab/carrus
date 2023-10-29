package com.ivangarzab.analytics

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class AnalyticsRepositoryTest {

    private lateinit var repository: TestAnalyticsRepository
    private var callbackResult: Boolean? = null

    @Before
    fun setup() {
        repository = TestAnalyticsRepository { callbackResult = it }
    }

    @Test
    fun test_isValidForAnalytics_empty_name_failure() {
        assertThat(repository.isValidForAnalytics(STRING_EMPTY))
            .isFalse()
    }

    @Test
    fun test_isValidForAnalytics_blank_name_failure() {
        assertThat(repository.isValidForAnalytics(STRING_BLANK))
            .isFalse()
    }

    @Test
    fun test_isValidForAnalytics_invalid_name_failure() {
        assertThat(repository.isValidForAnalytics(STRING_INVALID))
            .isFalse()
    }

    @Test
    fun test_isValidForAnalytics_valid_name_success() {
        assertThat(repository.isValidForAnalytics(STRING_VALID))
            .isTrue()
    }

    @Test
    fun test_callback_base() {
        assertThat(callbackResult)
            .isNull()
    }

    @Test
    fun test_logEvent_empty_name_failure() {
        repository.logEvent(STRING_EMPTY)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_blank_name_failure() {
        repository.logEvent(STRING_BLANK)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_invalid_name_failure() {
        repository.logEvent(STRING_INVALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_valid_name_success() {
        repository.logEvent(STRING_VALID)
        assertThat(callbackResult)
            .isTrue()
    }
    //TODO: Test params variable for logEvent

    @Test
    fun test_logScreenView_empty_screenName() {
        repository.logScreenView(STRING_EMPTY, STRING_VALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_blank_screenName() {
        repository.logScreenView(STRING_BLANK, STRING_VALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_invalid_screenName() {
        repository.logScreenView(STRING_INVALID, STRING_VALID)
        assertThat(callbackResult)
            .isFalse()
    }
    //
    @Test
    fun test_logScreenView_empty_screenClass() {
        repository.logScreenView(STRING_VALID, STRING_EMPTY)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_blank_screenClass() {
        repository.logScreenView(STRING_VALID, STRING_BLANK)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_invalid_screenClass() {
        repository.logScreenView(STRING_VALID, STRING_INVALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_valid_screenName_valid_screenClass_success() {
        repository.logScreenView(STRING_VALID, STRING_VALID)
        assertThat(callbackResult)
            .isTrue()
    }

    companion object {
        private const val STRING_EMPTY = ""
        private const val STRING_BLANK = "   "
        private const val STRING_INVALID = "Invalid String"
        private const val STRING_VALID = "valid_string"
    }
}