package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.remoteDataSource.response.RatingResponse
import com.hdd.globalmovie.data.remoteDataSource.response.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RatingService {
    @POST("rating/upsert/{productId}/{ratingNumber}")
    suspend fun addOrUpdateRating(
        @Header("Authorization") token: String,
        @Path("productId") productId: String,
        @Path("ratingNumber") ratingNumber: Int,
    ): Response<RatingResponse>

    @GET("rating/get/{productId}")
    suspend fun getRatingByProduct(
        @Header("Authorization") token: String,
        @Path("productId") productId: String,
    ): Response<RatingResponse>

}