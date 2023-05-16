package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.Rating

data class RatingResponse(
    val success: Boolean? = null,
    val data: Rating? = null,
    val previousRating: Int?=null,
)