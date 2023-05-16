package com.example.efashionwearos.retrofit.response

import com.example.efashionwearos.models.CartItem

data class CartResponse(
    val success: Boolean? = null,
    val data: MutableList<CartItem>? = null,
)