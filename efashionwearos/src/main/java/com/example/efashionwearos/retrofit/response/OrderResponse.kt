package com.example.efashionwearos.retrofit.response

import com.example.efashionwearos.models.Order

data class OrderResponse (
    val success: Boolean? = null,
    val data: MutableList<Order>? = null,
    val latestOrderId: String? = null,
)