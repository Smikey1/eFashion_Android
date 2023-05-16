package com.hdd.globalmovie

import com.hdd.globalmovie.data.models.User
import com.hdd.globalmovie.repository.CartRepository
import com.hdd.globalmovie.repository.ProductRepository
import com.hdd.globalmovie.repository.UserRepository
import com.hdd.globalmovie.repository.WishlistRepository
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class ValidationUnitTest {
    private val EMAIL_PATTERN="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
//    @Test
//    fun checkForValidEmail(){
//        val email :String = "admin@gmail.com"
//        val testResult = email.matches(EMAIL_PATTERN.toRegex())
//        assertThat(testResult).isTrue()
//    }

    @Test
    fun test(){
        val email :String = "admin@gmail.com"
        val testResult = email.matches(EMAIL_PATTERN.toRegex())
        val actual:Boolean =true
        val expected:Boolean = testResult
        assertEquals(expected,actual)
    }

    private lateinit var userRepository: UserRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var wishlistRepository: WishlistRepository

    @Test
    fun loginUser() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.loginUser(User(email="hello@text.com", password = "hello123"))
        val expectedResult = true
        val actualResult = response.success
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun registerUser() = runBlocking {
        val user = User(
                fullname = "Admin Admin",
                email = "admin@gmail.com",
                password = "admin123",
                confirmPassword = "admin123",
            )
        userRepository = UserRepository()
        val response = userRepository.registerUser(user)
        val expectedResult = true
        val actualResult = response.success
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getAllProduct() = runBlocking {
        productRepository = ProductRepository()
        val response = productRepository.getAllProduct()
        val expectedResult = true
        val actualResult = response?.isNotEmpty()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getUserProfile() = runBlocking {
        userRepository = UserRepository()
        val accessToken = "Bearer " + userRepository.loginUser(User(email="hello@text.com", password = "hello123")).accessToken
        ServiceBuilder.token=accessToken
        val response = userRepository.getUserProfile()
        val expectedResult = true
        val actualResult = response.success
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getAllCart() = runBlocking {
        cartRepository = CartRepository()
        userRepository = UserRepository()
        val accessToken = "Bearer " + userRepository.loginUser(User(email="hello@text.com", password = "hello123")).accessToken
        ServiceBuilder.token=accessToken
        val response = cartRepository.getAllCart()
        val expectedResult = true
        val actualResult = response.success
        assertEquals(expectedResult, actualResult)
    }

    // Before test this: make sure this product is in cart--> 61389f3b73efbe3950418650
    @Test
    fun deleteProductFromCart() = runBlocking {
        cartRepository = CartRepository()
        userRepository = UserRepository()
        val accessToken = "Bearer " + userRepository.loginUser(User(email="hello@text.com", password = "hello123")).accessToken
        ServiceBuilder.token=accessToken
        val response = cartRepository.deleteProductFromCart("61389f3b73efbe3950418650")
        val expectedResult = true
        val actualResult = response.success
        assertEquals(expectedResult, actualResult)
    }

//    @Test
//    fun addProductToWishlist() = runBlocking {
//        wishlistRepository = WishlistRepository()
//        userRepository = UserRepository()
//        val accessToken = "Bearer " + userRepository.loginUser(User(email="hello@text.com", password = "hello123")).accessToken
//        ServiceBuilder.token=accessToken
//        val response = wishlistRepository.addProductToWishlist("61389f3b73efbe3950418650")
//        val expectedResult = true
//        val actualResult = response.success
//        assertEquals(expectedResult, actualResult)
//    }

    @Test
    fun getAllWishlist() = runBlocking {
        wishlistRepository = WishlistRepository()
        userRepository = UserRepository()
        val accessToken = "Bearer " + userRepository.loginUser(User(email="hello@text.com", password = "hello123")).accessToken
        ServiceBuilder.token=accessToken
        val response = wishlistRepository.getAllWishlist()
        val expectedResult = true
        val actualResult = response.data?.isNotEmpty()
        assertEquals(expectedResult, actualResult)
    }

    // Before test this: make sure this product is in wishlist--> 61389f3b73efbe3950418650
    @Test
    fun deleteProductFromWishlist() = runBlocking {
        wishlistRepository = WishlistRepository()
        userRepository = UserRepository()
        val accessToken = "Bearer " + userRepository.loginUser(User(email="hello@text.com", password = "hello123")).accessToken
        ServiceBuilder.token=accessToken
        val response = wishlistRepository.deleteProductFromWishlist("61389f3b73efbe3950418650")
        val expectedResult = true
        val actualResult = response.success
        assertEquals(expectedResult, actualResult)
    }
}
