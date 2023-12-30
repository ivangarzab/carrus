package com.ivangarzab.carrus.util.managers

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.memory.MemoryCache

/**
 * Created by Ivan Garza Bermea.
 */
class Coiler {
    fun setImageLoader(context: Context) {
        Coil.setImageLoader(
            ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .build()
        )
    }
}