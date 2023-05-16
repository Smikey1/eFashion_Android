package com.example.efashionwearos.retrofit.services

import com.example.efashionwearos.models.User
import com.example.efashionwearos.retrofit.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserServices {

    @POST("user/login")
    suspend fun login(@Body user: User): Response<UserResponse>

    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
    ):Response<UserResponse>

}
