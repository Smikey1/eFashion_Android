package com.hdd.globalmovie.data.models


data class Review(
    val _id:String="",
    val review:String,
    val rating:Int=0,
    val reviewDate: String?=null,
    val productId:Product?=null,
    val userId: User?=null,
)
