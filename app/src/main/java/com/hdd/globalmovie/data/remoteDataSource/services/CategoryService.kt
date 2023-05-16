package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.remoteDataSource.response.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CategoryService {
    @GET("category/get")
    suspend fun getAllCategory():Response<CategoryResponse>

}