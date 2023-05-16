package com.hdd.globalmovie.domain.viewModel.banner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hdd.globalmovie.repository.BannerRepository
import com.hdd.globalmovie.repository.ProductRepository

class BannerVMF (val repository: BannerRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(BannerVM::class.java)){
            return BannerVM(repository) as T
        }
        throw IllegalAccessException("Unknown View Model Class")
    }
}