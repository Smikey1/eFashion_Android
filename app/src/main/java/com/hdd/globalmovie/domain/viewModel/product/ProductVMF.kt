package com.hdd.globalmovie.domain.viewModel.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hdd.globalmovie.repository.ProductRepository

class ProductVMF (val repository: ProductRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(ProductVM::class.java)){
            return ProductVM(repository) as T
        }
        throw IllegalAccessException("Unknown View Model Class")
    }
}