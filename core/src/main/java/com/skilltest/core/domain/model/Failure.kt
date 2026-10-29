package com.skilltest.core.domain.model

open class Failure(
    val msg: String? = null,
    val exception: Exception? = null,
    val code: Int? = null
)
