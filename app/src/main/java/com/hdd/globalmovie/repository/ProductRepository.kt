package com.hdd.globalmovie.repository

import com.hdd.globalmovie.data.localDataSource.ProductDAO
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.data.remoteDataSource.HttpRequestNetworkCall
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.data.remoteDataSource.response.ProductResponse
import com.hdd.globalmovie.data.remoteDataSource.services.ProductService

class ProductRepository(private val productDao: ProductDAO? = null) : HttpRequestNetworkCall() {
    private val productService = ServiceBuilder.buildService(ProductService::class.java)
    suspend fun getAllProduct(): MutableList<Product>? {
        try {
            val response = myHttpRequestNetworkCall {
                productService.getAllProduct()
            }
            if (response.success == true) {
                insertProduct(response.data!!)
            }
            return productDao?.fetchAllProduct()
        } catch (ex: Exception) {
            print(ex)
        }
        return productDao?.fetchAllProduct()
    }

    suspend fun getProductByPattern(pattern: String): ProductResponse {
        return myHttpRequestNetworkCall {
            productService.getProductByPattern(pattern)
        }
    }

    suspend fun getProductById(id: String): ProductResponse {
        return myHttpRequestNetworkCall {
            productService.getProductById(id)
        }
    }

    //Insert Product to Room Database
    private suspend fun insertProduct(productsList: MutableList<Product>) {
        for (product in productsList) {
            productDao?.insertProduct(product)
        }
    }

    //get all products by category
    suspend fun getAllProductByCategory(category: String): ProductResponse {
        return myHttpRequestNetworkCall {
            productService.fetchProductByCategory(category)
        }
    }

}

