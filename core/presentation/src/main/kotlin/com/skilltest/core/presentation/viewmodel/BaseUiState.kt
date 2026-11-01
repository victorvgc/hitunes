package com.skilltest.core.presentation.viewmodel

abstract class BaseUiState(
    open val name: String,
    val createdAt: Long = System.nanoTime()
) {
}
