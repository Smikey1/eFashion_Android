package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.Category

data class CategoryResponse(
    val success: Boolean? = null,
    val data: List<Category>? = null
)