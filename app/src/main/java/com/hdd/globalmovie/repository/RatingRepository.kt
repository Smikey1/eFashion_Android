package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.RatingResponse
import com.hdd.globalmovie.data.remoteDataSource.services.RatingService


class RatingRepository : HttpRequestNetworkCall() {
    private val ratingService = ServiceBuilder.buildService(RatingService::class.java)

    suspend fun getRatingByProduct(productId:String): RatingResponse {
        return myHttpRequestNetworkCall {
            ratingService.getRatingByProduct(ServiceBuilder.token!!,productId)
        }
    }

    suspend fun addOrUpdateRating(productId:String,ratingNumber:Int): RatingResponse {
        return myHttpRequestNetworkCall {
            ratingService.addOrUpdateRating(ServiceBuilder.token!!,productId,ratingNumber)
        }
    }

}