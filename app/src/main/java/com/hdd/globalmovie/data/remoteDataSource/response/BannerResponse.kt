package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.Banner

data class BannerResponse(
    val success:Boolean?=null,
    val data:MutableList<String>?=null,
)
