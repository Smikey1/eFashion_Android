package com.hdd.globalmovie.data.remoteDataSource.response

import com.hdd.globalmovie.data.models.Product

data class ProductResponse(
    val success: Boolean? = null,
    val data: MutableList<Product>? = null,
    val singleProductData: Product? = null,
)