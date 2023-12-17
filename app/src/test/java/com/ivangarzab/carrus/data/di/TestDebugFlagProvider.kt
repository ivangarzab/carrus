package com.ivangarzab.carrus.data.di

class TestDebugFlagProvider : DebugFlagProvider {

    override var forceDebug: Boolean = false

    var internalFlag: Boolean = false

    override fun isDebugEnabled(): Boolean = when(forceDebug) {
        true -> true
        false -> internalFlag
    }
}