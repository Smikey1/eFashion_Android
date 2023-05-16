package com.hdd.globalmovie.domain.viewModel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hdd.globalmovie.repository.CartRepository
import com.hdd.globalmovie.domain.viewModel.cart.CartVM

class OrderVMF (val repository: CartRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(CartVM::class.java)){
            return CartVM(repository) as T
        }
        throw IllegalAccessException("Unknown View Model Class")
    }
}