package com.ivangarzab.carrus

import com.ivangarzab.carrus.util.managers.Coiler
import com.ivangarzab.carrus.util.managers.LeakUploader
import com.ivangarzab.carrus.util.managers.NightThemeManager

/**
 * Created by Ivan Garza Bermea.
 */
class TestApp(
    lu: LeakUploader = LeakUploader(),
    c: Coiler = Coiler(),
    ntm: NightThemeManager
) : App() {
    override val leakUploader = lu
    override val coiler = c
    override val nightThemeManager = ntm
}
