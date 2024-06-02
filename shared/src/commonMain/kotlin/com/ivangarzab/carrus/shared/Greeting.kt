package com.ivangarzab.carrus.shared

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, Carrus! \n\nFrom ${platform.name}."
    }
}