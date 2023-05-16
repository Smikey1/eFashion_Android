package com.example.efashionwearos.models

data class Product(
    val _id:String="",
    val productImageUrlList: List<String>? = null,
    val productName: String? = null,
    val productPrice: Int? = null,
    val productCategory: String? = null,
    val productCuttedPrice: Int?=null,
    val productDescription: String? = null,
    val productColor:String?=null,
    val freeCoupons: Int = 0,
    val featureName:String?=null,
    val featureValue: String? =null,
    val productQuantity: Int = 1,
    val offerApplied: Int = 0,
    val productShortDescription: String?=null,
    val productSpecification: String?=null,
    val productOtherDetails: String?=null,
    val paymentMethod:String?=null,
    val productRating:Double=0.0,
    val totalRating:Int=0,
)
