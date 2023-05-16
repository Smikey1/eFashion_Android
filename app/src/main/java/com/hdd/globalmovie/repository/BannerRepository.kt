package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.BannerResponse
import com.hdd.globalmovie.data.remoteDataSource.services.BannerService

class BannerRepository : HttpRequestNetworkCall() {
    private val bannerService = ServiceBuilder.buildService(BannerService::class.java)

    suspend fun getAllBanner(categoryName:String): BannerResponse {
        return myHttpRequestNetworkCall {
            bannerService.getAllBanner(categoryName)
        }
    }
}