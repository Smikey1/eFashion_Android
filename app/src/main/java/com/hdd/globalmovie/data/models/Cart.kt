package com.hdd.globalmovie.data.models

data class Cart(
    val _id:String?="",
    val totalItem:Int?=null,
    val totalItemPrice:String?=null,
    val deliveryPrice:String?=null,
    val savedAmount:String?=null,
    val grandTotal:String?=null,
)
