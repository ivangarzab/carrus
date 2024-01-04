package com.ivangarzab.carrus

import com.ivangarzab.carrus.util.managers.Coiler
import com.ivangarzab.carrus.util.managers.LeakUploader

/**
 * Created by Ivan Garza Bermea.
 */
class TestApp(
    lu: LeakUploader = LeakUploader(),
    c: Coiler = Coiler()
) : App() {
    override val leakUploader = lu
    override val coiler = c
}
