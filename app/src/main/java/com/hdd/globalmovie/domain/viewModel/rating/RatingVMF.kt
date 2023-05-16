package com.hdd.globalmovie.domain.viewModel.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hdd.globalmovie.repository.RatingRepository

class RatingVMF (val repository: RatingRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(RatingVM::class.java)){
            return RatingVM(repository) as T
        }
        throw IllegalAccessException("Unknown View Model Class")
    }
}