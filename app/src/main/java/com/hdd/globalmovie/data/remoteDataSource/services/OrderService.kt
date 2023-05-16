package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.models.Order
import com.hdd.globalmovie.data.remoteDataSource.response.OrderResponse
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