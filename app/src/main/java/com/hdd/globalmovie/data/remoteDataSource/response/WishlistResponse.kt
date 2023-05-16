package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.WishlistItem

data class WishlistResponse(
    val success: Boolean? = null,
    val data: MutableList<WishlistItem>? = null,
)