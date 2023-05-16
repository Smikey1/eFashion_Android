package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.remoteDataSource.response.CartResponse
import retrofit2.Response
import retrofit2.http.*

interface CartService {
    @POST("cart/insert/{id}")
    suspend fun addProductToCart(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<CartResponse>

    @GET("cart/get")
    suspend fun getAllCart(
        @Header("Authorization") token: String,
    ): Response<CartResponse>

    @PUT("cart/update/{id}/{quantityNumber}")
    suspend fun updateQuantity(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Path("quantityNumber") quantityNumber: Int,
    ): Response<CartResponse>

    @DELETE("cart/delete/{id}")
    suspend fun deleteProductFromCart(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<CartResponse>
}