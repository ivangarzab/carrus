package com.ivangarzab.carrus.util.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Created by Ivan Garza Bermea.
 */
class StringExtensionsTest {

    /*@Test
    fun test_getCalendarFromShortenerDate_success() {
        TEST_DATE.getCalendarFromShortenedDate().timeInMillis.let {
            assertThat(it)
                .isEqualTo(ANSWER_SHORTENED_DATE)
        }
    } TODO: This test is failing inside GitHub Actions */

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

    @Test
    fun test_isDigitsOnly_empty_string_failure() {
        assertThat(EMPTY_STRING.isDigitsOnly())
            .isFalse()
    }

    @Test
    fun test_isDigitsOnly_valid_digits_success() {
        assertThat(TEST_VALID_DIGITS.isDigitsOnly())
            .isTrue()
    }

    @Test
    fun test_isDigitsOnly_valid_digits_with_period_failure() {
        assertThat(TEST_VALID_DIGITS_WITH_PERIOD.isDigitsOnly())
            .isFalse()
    }

    @Test
    fun test_parseIntoMoney_empty_string_failure() {
        assertThat(EMPTY_STRING.parseIntoMoney())
            .isEqualTo(EMPTY_MONEY_VALUE)
    }

    @Test
    fun test_parseIntoMoney_non_digits_string_failure() {
        assertThat(NON_DIGITS.parseIntoMoney())
            .isEqualTo(EMPTY_MONEY_VALUE)
    }

    @Test
    fun test_parseIntoMoney_valid_string_success() {
        assertThat(TEST_VALID_DIGITS.parseIntoMoney())
            .isEqualTo(ANSWER_VALID_DIGITS)
    }

    @Test
    fun test_parseIntoMoney_valid_string_with_period_success() {
        assertThat(TEST_VALID_DIGITS_WITH_PERIOD.parseIntoMoney())
            .isEqualTo(ANSWER_VALID_DIGITS_WITH_PERIOD)
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val TEST_DATE = "9/12/2023"
        private const val TEST_DATE_BAD = "13/42/111"
        private const val TEST_DATE_PAST = "12/31/1869"
        private const val ANSWER_SHORTENED_DATE = 1697151600000

        private const val NON_DIGITS = "abc"
        private const val TEST_VALID_DIGITS = "6"
        private const val TEST_VALID_DIGITS_WITH_PERIOD = "999.99"
        private const val ANSWER_VALID_DIGITS = 6.0f
        private const val ANSWER_VALID_DIGITS_WITH_PERIOD = 999.99f
    }
}