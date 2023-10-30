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
    fun test_logEvent_empty_name_no_params_failure() {
        repository.logEvent(STRING_EMPTY)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_blank_name_no_params_failure() {
        repository.logEvent(STRING_BLANK)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_invalid_name_no_params_failure() {
        repository.logEvent(STRING_INVALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_valid_name_no_params_success() {
        repository.logEvent(STRING_VALID)
        assertThat(callbackResult)
            .isTrue()
    }

    @Test
    fun test_logEvent_valid_name_empty_param_failure() {
        repository.logEvent(
            name = STRING_VALID,
            params = arrayOf(STRING_EMPTY to STRING_EMPTY)
        )
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_valid_name_blank_param_failure() {
        repository.logEvent(
            name = STRING_VALID,
            params = arrayOf(STRING_BLANK to STRING_EMPTY)
        )
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_valid_name_invalid_param_failure() {
        repository.logEvent(
            name = STRING_VALID,
            params = arrayOf(STRING_INVALID to STRING_EMPTY)
        )
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_valid_name_valid_param_success() {
        repository.logEvent(
            name = STRING_VALID,
            params = arrayOf(STRING_VALID to STRING_EMPTY)
        )
        assertThat(callbackResult)
            .isTrue()
    }

    @Test
    fun test_logEvent_valid_name_two_params_second_invalid_success() {
        repository.logEvent(
            name = STRING_VALID,
            params = arrayOf(
                STRING_VALID to STRING_EMPTY,
                STRING_INVALID to STRING_EMPTY,
            )
        )
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logEvent_valid_name_two_valid_params_success() {
        repository.logEvent(
            name = STRING_VALID,
            params = arrayOf(
                STRING_VALID to STRING_EMPTY,
                STRING_VALID to STRING_EMPTY,
            )
        )
        assertThat(callbackResult)
            .isTrue()
    }

    @Test
    fun test_logScreenView_empty_screenName_valid_screenClass() {
        repository.logScreenView(STRING_EMPTY, STRING_VALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_blank_screenName_valid_screenClass() {
        repository.logScreenView(STRING_BLANK, STRING_VALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_valid_screenName_empty_screenClass() {
        repository.logScreenView(STRING_VALID, STRING_EMPTY)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_valid_screenName_blank_screenClass() {
        repository.logScreenView(STRING_VALID, STRING_BLANK)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_valid_screenName_invalid_screenClass() {
        repository.logScreenView(STRING_VALID, STRING_INVALID)
        assertThat(callbackResult)
            .isFalse()
    }

    @Test
    fun test_logScreenView_invalid_screenName_invalid_screenClass() {
        repository.logScreenView(STRING_INVALID, STRING_INVALID)
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