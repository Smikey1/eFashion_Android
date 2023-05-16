package com.hdd.globalmovie.data.models

data class CartItem(
    val _id:String="",
    val quantity:Int =1,
    val productId:Product?=null,
    val userId :String?=null
)
