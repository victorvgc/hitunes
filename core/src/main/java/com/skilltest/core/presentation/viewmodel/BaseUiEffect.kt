package com.skilltest.core.presentation.viewmodel

abstract class BaseUiEffect(
    open val name: String,
    val createdAt: Long = System.nanoTime()
) {
}
