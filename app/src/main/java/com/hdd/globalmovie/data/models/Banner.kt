package com.hdd.globalmovie.data.models

data class Banner(
    val _id:String="",
    val bannerImageUrlList:MutableList<String>?=null,
    val bannerName:String?=null
)
