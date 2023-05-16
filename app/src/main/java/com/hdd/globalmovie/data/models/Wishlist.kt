package com.hdd.globalmovie.data.models

data class Wishlist(
    val _id:String?=null,
    val productImageUrl: String?=null,
    val productName:String?=null,
    val productPrice:Int?=null,
    val paymentMethod:String?=null,
    val cuttedPrice:Int? = null,
    val freeCoupons:Int = 0,
    val productRating:Int=0,
    val totalRating:Int=0,
)
