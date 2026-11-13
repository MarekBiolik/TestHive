package com.testhive.core

object TestResources {
    fun read(path: String): String {
        val loader = TestResources::class.java.classLoader
            ?: error("No class loader for test resources")
        val stream = loader.getResourceAsStream(path)
            ?: error("Missing test resource: $path")
        return stream.bufferedReader().use { it.readText() }
    }
}
