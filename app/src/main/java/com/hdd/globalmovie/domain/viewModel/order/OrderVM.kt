package com.hdd.globalmovie.domain.viewModel.order

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.hdd.globalmovie.data.models.CartItem
import com.hdd.globalmovie.repository.CartRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderVM(private val cartRepository: CartRepository) : ViewModel(),Observable {

        var cartList = MutableLiveData<MutableList<CartItem>>()
        var cart= MutableLiveData<CartItem>()

        @Bindable
        var allCartPrice = MutableLiveData<String>()

        fun getAllCart() = viewModelScope.launch {
            var result: MutableList<CartItem>? = null
            withContext(IO) {
                val response = cartRepository.getAllCart()
                if (response.success == true) {
                    result = response.data!!
                }
            }
            for (myCart in result!!){
                cart.value=myCart
            }

            val totalPrice = result!!.map {
                it.productId!!.productPrice!!.toInt().times(it.quantity)
            }.sum()

            allCartPrice.value = "Rs. $totalPrice.00"
            cartList.value = result!!
        }

        override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

        }

        override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

        }

}

