package com.ivangarzab.carrus.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform