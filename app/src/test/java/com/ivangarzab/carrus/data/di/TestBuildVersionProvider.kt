package com.ivangarzab.carrus.data.di

class TestBuildVersionProvider : BuildVersionProvider {
    override fun getSdkVersionInt(): Int = TEST_SDK_VERSION

    companion object {
        const val TEST_SDK_VERSION = 6
    }
}