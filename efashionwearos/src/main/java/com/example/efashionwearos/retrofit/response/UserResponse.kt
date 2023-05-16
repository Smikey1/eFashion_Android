package com.example.efashionwearos.retrofit.response

import com.example.efashionwearos.models.User

data class UserResponse(
    val success: Boolean? = null,
    val data: User? = null,
    val accessToken: String? = null
)