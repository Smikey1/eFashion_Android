package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.Order

data class OrderResponse (
    val success: Boolean? = null,
    val data: MutableList<Order>? = null,
    val latestOrderId: String? = null,
)