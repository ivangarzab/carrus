package com.ivangarzab.carrus.util.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class StringExtensionsTest {

    @Test
    fun test_getCalendarFromShortenerDate_success() {
        assertThat(TEST_DATE.getCalendarFromShortenedDate().timeInMillis)
            .isEqualTo(ANSWER_SHORTENED_DATE)
    }

    @Test
    fun test_getCalendarFromShortenerDate_past_unix_time_failure() {
        assertThat(TEST_DATE_PAST.getCalendarFromShortenedDate().timeInMillis)
            .isLessThan(0L)
    }

    @Test
    fun test_getCalendarFromShortenerDate_bad_date_failure() {
        assertThat(TEST_DATE_BAD.getCalendarFromShortenedDate().timeInMillis)
            .isLessThan(0L)
    }

    @Test
    fun test_getCalendarFromShortenerDate_empty_string_failure() {
        assertThat(EMPTY_STRING.getCalendarFromShortenedDate().timeInMillis)
            .isEqualTo(0L)
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val TEST_DATE = "9/12/2023"
        private const val TEST_DATE_BAD = "13/42/111"
        private const val TEST_DATE_PAST = "12/31/1869"

        private const val ANSWER_SHORTENED_DATE = 1697151600000
    }
}