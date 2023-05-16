package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.remoteDataSource.response.BannerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BannerService {

    @GET("banner/categoryName/{categoryName}")
    suspend fun getAllBanner(
        @Path("categoryName") categoryName:String,
    ): Response<BannerResponse>

}