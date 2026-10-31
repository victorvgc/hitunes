package com.skilltest.core.domain.model

open class Failure(
    open val msg: String? = null,
    open val exception: Exception? = null,
    open val code: Int? = null
)
