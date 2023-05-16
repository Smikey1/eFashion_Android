package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.remoteDataSource.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("product/get")
    suspend fun getAllProduct(): Response<ProductResponse>

    @GET("product/getById/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductResponse>

    @GET("product/search/{pattern}")
    suspend fun getProductByPattern(@Path("pattern") pattern: String): Response<ProductResponse>

    @GET("product/category/{categoryName}")
    suspend fun fetchProductByCategory(@Path("categoryName") categoryName: String):Response<ProductResponse>
}