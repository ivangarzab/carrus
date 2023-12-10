package com.ivangarzab.carrus.data.di

import com.google.common.truth.Truth
import com.ivangarzab.carrus.util.helpers.ContentResolverHelper
import com.ivangarzab.carrus.util.helpers.ContentResolverHelperImpl
import io.mockk.mockk
import org.junit.Test

class ContentResolverHelperModuleTest {

    private val helper: ContentResolverHelperImpl = mockk()

    private val module = object : ContentResolverHelperModule() {
        override fun bindContentResolverHelper(
            contentResolverHelper: ContentResolverHelperImpl
        ): ContentResolverHelper = helper

    }

    @Test
    fun test_binding() {
        val result = module.bindContentResolverHelper(helper)
        Truth.assertThat(result)
            .isNotNull()
    }
}