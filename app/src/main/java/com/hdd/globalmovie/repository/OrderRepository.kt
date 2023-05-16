package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.models.Order
import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.OrderResponse
import com.hdd.globalmovie.data.remoteDataSource.services.OrderService


class OrderRepository : HttpRequestNetworkCall() {
    private val orderService = ServiceBuilder.buildService(OrderService::class.java)

    suspend fun getAllOrder(): OrderResponse {
        return myHttpRequestNetworkCall {
            orderService.getAllOrder(ServiceBuilder.token!!)
        }
    }


    suspend fun addProductToOrder(orderItem:Order): OrderResponse {
        return myHttpRequestNetworkCall {
            orderService.addProductToOrder(ServiceBuilder.token!!,orderItem)
        }
    }
}