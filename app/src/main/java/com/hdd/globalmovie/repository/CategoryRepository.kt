package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.CategoryResponse
import com.hdd.globalmovie.data.remoteDataSource.services.CategoryService

class CategoryRepository : HttpRequestNetworkCall() {
    private val categoryService = ServiceBuilder.buildService(CategoryService::class.java)
    suspend fun getAllCategory(): CategoryResponse {
        return myHttpRequestNetworkCall {
            categoryService.getAllCategory()
        }
    }
}