package com.hdd.globalmovie.domain.viewModel.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hdd.globalmovie.repository.CartRepository

class CartVMF (val repository: CartRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(CartVM::class.java)){
            return CartVM(repository) as T
        }
        throw IllegalAccessException("Unknown View Model Class")
    }
}