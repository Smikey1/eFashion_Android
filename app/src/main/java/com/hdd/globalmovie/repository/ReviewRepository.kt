package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.models.Review
import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.ReviewResponse
import com.hdd.globalmovie.data.remoteDataSource.services.ReviewService


class ReviewRepository : HttpRequestNetworkCall() {
    private val reviewService = ServiceBuilder.buildService(ReviewService::class.java)

    suspend fun getAllReview(): ReviewResponse {
        return myHttpRequestNetworkCall {
            reviewService.getAllReview()
        }
    }

    suspend fun addReview(productId:String,review:Review): ReviewResponse {
        return myHttpRequestNetworkCall {
            reviewService.addReview(ServiceBuilder.token!!,productId,review)
        }
    }

    suspend fun updateReview(reviewId:String,review:Review): ReviewResponse {
        return myHttpRequestNetworkCall {
            reviewService.updateReview(ServiceBuilder.token!!,reviewId,review)
        }
    }

    suspend fun deleteReview(reviewId:String): ReviewResponse {
        return myHttpRequestNetworkCall {
            reviewService.deleteReview(ServiceBuilder.token!!,reviewId)
        }
    }
}