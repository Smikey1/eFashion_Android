package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.Review

data class ReviewResponse(
    val success: Boolean? = null,
    val data: MutableList<Review>? = null,
)