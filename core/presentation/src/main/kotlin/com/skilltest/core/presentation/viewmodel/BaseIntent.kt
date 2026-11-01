package com.skilltest.core.presentation.viewmodel

abstract class BaseIntent(
    open val name: String,
    val createdAt: Long = System.nanoTime()
) {
    init {
        require(name.isNotEmpty(), { "Name must not be empty!" })
    }
}
