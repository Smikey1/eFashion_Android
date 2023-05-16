package com.example.efashionwearos.repository

import com.example.efashionwearos.retrofit.HttpRequestNetworkCall
import com.example.efashionwearos.retrofit.ServiceBuilder
import com.example.efashionwearos.retrofit.response.OrderResponse
import com.example.efashionwearos.retrofit.services.OrderService


class OrderRepository : HttpRequestNetworkCall() {
    private val orderService = ServiceBuilder.buildService(OrderService::class.java)

    suspend fun getAllOrder(): OrderResponse {
        return myHttpRequestNetworkCall {
            orderService.getAllOrder(ServiceBuilder.token!!)
        }
    }

}