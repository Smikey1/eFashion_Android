package com.hdd.globalmovie.data.models


data class Order(
    val _id:String?=null,
    val deliveryStatusMessage:String?=null,
    val userId: User?=null,
    val order: List<OrderItem>,
)

data class OrderItem(
    val _id: String?=null,
    val productId: Product,
    val qty: Int =1
)
