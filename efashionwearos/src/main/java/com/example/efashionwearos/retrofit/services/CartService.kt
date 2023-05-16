package com.example.efashionwearos.retrofit.services

import com.example.efashionwearos.retrofit.response.CartResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CartService {

    @GET("cart/get")
    suspend fun getAllCart(
        @Header("Authorization") token: String,
    ): Response<CartResponse>

}