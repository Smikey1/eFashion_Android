package com.example.efashionwearos.retrofit.services

import com.example.efashionwearos.models.Order
import com.example.efashionwearos.retrofit.response.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderService {
    @POST("order/insert/")
    suspend fun addProductToOrder(
        @Header("Authorization") token: String,
        @Body orderItem: Order
    ): Response<OrderResponse>

    @GET("order/get")
    suspend fun getAllOrder(
        @Header("Authorization") token: String,
    ): Response<OrderResponse>

}