package com.example.efashionwearos.repository

import com.example.efashionwearos.models.User
import com.example.efashionwearos.retrofit.HttpRequestNetworkCall
import com.example.efashionwearos.retrofit.ServiceBuilder
import com.example.efashionwearos.retrofit.response.UserResponse
import com.example.efashionwearos.retrofit.services.UserServices


class UserRepository : HttpRequestNetworkCall() {
    private val userService = ServiceBuilder.buildService(UserServices::class.java)

    suspend fun loginUser(user: User): UserResponse {
        return myHttpRequestNetworkCall {
            userService.login(user)
        }
    }
    suspend fun getUserProfile(): UserResponse {
        return myHttpRequestNetworkCall {
            userService.getUserProfile(ServiceBuilder.token!!)
        }
    }

}