package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.remoteDataSource.response.WishlistResponse
import retrofit2.Response
import retrofit2.http.*

interface WishlistService {
    @POST("wishlist/insert/{id}")
    suspend fun addProductToWishlist(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<WishlistResponse>

    @GET("wishlist/get")
    suspend fun getAllWishlist(
        @Header("Authorization") token: String,
    ): Response<WishlistResponse>

    @DELETE("wishlist/delete/{id}")
    suspend fun deleteProductFromWishlist(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<WishlistResponse>
}