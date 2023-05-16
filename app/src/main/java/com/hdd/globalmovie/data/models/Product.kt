package com.hdd.globalmovie.data.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.annotations.Nullable

@Entity
data class Product(
    @Nullable
    @PrimaryKey
    var _id:String="",
    var productImageUrlList: List<String>? = null,
    var productName: String? = null,
    var productPrice: Int? = null,
    var productCategory: String? = null,
    var productCuttedPrice: Int?=null,
    var productDescription: String? = null,
    var productColor:String?=null,
    var freeCoupons: Int = 0,
    var featureName:String?=null,
    var featureValue: String? =null,
    var productQuantity: Int = 1,
    var offerApplied: Int = 0,
    var productShortDescription: String?=null,
    var productSpecification: String?=null,
    var productOtherDetails: String?=null,
    var paymentMethod:String?=null,
    var productRating:Double=0.0,
    var totalRating:Int=0,

    @Ignore
    var reviews:MutableList<Review>?=null
)

class ListToStringConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}