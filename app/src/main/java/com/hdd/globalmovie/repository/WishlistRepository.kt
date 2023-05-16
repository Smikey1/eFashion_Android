package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.WishlistResponse
import com.hdd.globalmovie.data.remoteDataSource.services.WishlistService


class WishlistRepository : HttpRequestNetworkCall() {
    private val wishlistService = ServiceBuilder.buildService(WishlistService::class.java)

    suspend fun getAllWishlist(): WishlistResponse {
        return myHttpRequestNetworkCall {
            wishlistService.getAllWishlist(ServiceBuilder.token!!)
        }
    }

    suspend fun addProductToWishlist(id:String): WishlistResponse {
        return myHttpRequestNetworkCall {
            wishlistService.addProductToWishlist(ServiceBuilder.token!!,id)
        }
    }
    suspend fun deleteProductFromWishlist(id:String): WishlistResponse {
        return myHttpRequestNetworkCall {
            wishlistService.deleteProductFromWishlist(ServiceBuilder.token!!,id)
        }
    }
}