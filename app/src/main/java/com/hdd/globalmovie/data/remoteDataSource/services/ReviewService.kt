package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.models.Review
import com.hdd.globalmovie.data.remoteDataSource.response.ReviewResponse
import retrofit2.Response
import retrofit2.http.*

interface ReviewService {
    @POST("review/insert/{id}")
    suspend fun addReview(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body review: Review
    ): Response<ReviewResponse>

    @GET("review/get")
    suspend fun getAllReview(
    ): Response<ReviewResponse>

    @PUT("review/update/{id}")
    suspend fun updateReview(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body review: Review
    ): Response<ReviewResponse>

    @DELETE("review/delete/{id}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ReviewResponse>

}