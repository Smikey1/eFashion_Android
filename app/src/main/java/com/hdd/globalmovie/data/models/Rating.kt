package com.hdd.globalmovie.data.models

data class Rating(
    val _id:String="",
    val rating: Int,
    val ratingDate: String?=null,
    val productId:Product?=null,
    val userId: User?=null,
)
