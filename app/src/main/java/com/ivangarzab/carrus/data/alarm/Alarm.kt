package com.ivangarzab.carrus.data.alarm

/**
 * Created by Ivan Garza Bermea.
 */
enum class Alarm(
    val requestCode: Int,
    val intentAction: String
) {
    TEST(
        requestCode = 0,
        intentAction = "alarm-test"
    ),
    PAST_DUE(
        requestCode = 200,
        intentAction = "alarm-past-due"
    ),
    ;
}