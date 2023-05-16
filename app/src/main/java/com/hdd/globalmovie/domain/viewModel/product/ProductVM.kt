package com.hdd.globalmovie.domain.viewModel.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProductVM(private val productRepository: ProductRepository): ViewModel(){
    var productList = MutableLiveData<MutableList<Product>>()
    var productCategoryList = MutableLiveData<MutableList<Product>>()
    var productSearchList = MutableLiveData<MutableList<Product>>()
    fun getAllProduct() = viewModelScope.launch {
        var result:MutableList<Product>?=null
        withContext(Dispatchers.IO){
            val response = productRepository.getAllProduct()
                result=response
        }
        productList.value=result!!

    }

    fun getProductByPattern(pattern:String) = viewModelScope.launch {
        var result:MutableList<Product>?=null
        withContext(Dispatchers.IO){
            val response = productRepository.getProductByPattern(pattern)
            if (response.success==true){
                result=response.data
            }
        }
        productSearchList.value=result!!

    }

    fun getProductByCategory(categoryName: String) = viewModelScope.launch {
        var result:MutableList<Product>?=null
        withContext(Dispatchers.IO){
            val response = productRepository.getAllProductByCategory(categoryName)
            if (response.success==true){
                result=response.data
            }
        }
        productCategoryList.value=result!!
    }
}

