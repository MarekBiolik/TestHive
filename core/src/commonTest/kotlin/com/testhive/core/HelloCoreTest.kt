package com.testhive.core

import kotlin.test.Test
import kotlin.test.assertEquals

class HelloCoreTest {
    @Test
    fun productName_isTestHive() {
        assertEquals("TestHive", HelloCore.productName)
    }
}
