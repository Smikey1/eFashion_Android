package com.example.efashionwearos.repository

import com.example.efashionwearos.retrofit.HttpRequestNetworkCall
import com.example.efashionwearos.retrofit.ServiceBuilder
import com.example.efashionwearos.retrofit.response.CartResponse
import com.example.efashionwearos.retrofit.services.CartService


class CartRepository : HttpRequestNetworkCall() {
    private val cartService = ServiceBuilder.buildService(CartService::class.java)

    suspend fun getAllCart(): CartResponse {
        return myHttpRequestNetworkCall {
            cartService.getAllCart(ServiceBuilder.token!!)
        }
    }
}