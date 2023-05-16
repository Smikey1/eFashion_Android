package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.CartItem

data class CartResponse(
    val success: Boolean? = null,
    val data: MutableList<CartItem>? = null,
)