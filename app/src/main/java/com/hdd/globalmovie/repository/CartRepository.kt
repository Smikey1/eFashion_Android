package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.CartResponse
import com.hdd.globalmovie.data.remoteDataSource.services.CartService


class CartRepository : HttpRequestNetworkCall() {
    private val cartService = ServiceBuilder.buildService(CartService::class.java)

    suspend fun getAllCart(): CartResponse {
        return myHttpRequestNetworkCall {
            cartService.getAllCart(ServiceBuilder.token!!)
        }
    }

    suspend fun updateQuantity(id:String,qtyNumber:Int): CartResponse {
        return myHttpRequestNetworkCall {
            cartService.updateQuantity(ServiceBuilder.token!!,id,qtyNumber)
        }
    }

    suspend fun addProductToCart(id:String): CartResponse {
        return myHttpRequestNetworkCall {
            cartService.addProductToCart(ServiceBuilder.token!!,id)
        }
    }
    suspend fun deleteProductFromCart(id:String): CartResponse {
        return myHttpRequestNetworkCall {
            cartService.deleteProductFromCart(ServiceBuilder.token!!,id)
        }
    }
}