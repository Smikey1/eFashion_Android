package com.hdd.globalmovie.domain.viewModel.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hdd.globalmovie.repository.ProductRepository

class ReviewVMF (val repository: ProductRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(ReviewVM::class.java)){
            return ReviewVM(repository) as T
        }
        throw IllegalAccessException("Unknown View Model Class")
    }
}