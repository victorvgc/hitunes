package com.skilltest.core.presentation.viewmodel

import java.sql.Date

abstract class BaseIntent(
    open val name: String,
    val createdAt: Long = System.nanoTime()
) {
}
