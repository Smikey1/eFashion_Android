package com.hdd.globalmovie.data.remoteDataSource.services

import com.hdd.globalmovie.data.models.User
import com.hdd.globalmovie.data.remoteDataSource.response.ImageResponse
import com.hdd.globalmovie.data.remoteDataSource.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserServices {
    @POST("user/register")
    suspend fun registerUser(
        @Body user: User,
    ): Response<UserResponse>

    @POST("user/login")
    suspend fun login(@Body user: User): Response<UserResponse>

    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
    ):Response<UserResponse>

    @Multipart
    @PUT("user/profile/upload/{id}")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<ImageResponse>

    @PUT("user/update/{id}")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Path("id") id:String,
        @Body user: User
    ):Response<UserResponse>

    @PUT("user/change-password/{id}")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body user: User,
    ):Response<UserResponse>
}
