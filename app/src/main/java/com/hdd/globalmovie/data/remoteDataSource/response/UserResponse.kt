package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.User

data class UserResponse(
    val success: Boolean? = null,
    val data: User? = null,
    val accessToken: String? = null
)