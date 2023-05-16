package com.hdd.globalmovie.domain.viewModel.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hdd.globalmovie.data.models.Review
import com.hdd.globalmovie.repository.ProductRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReviewVM(private val productRepository: ProductRepository) : ViewModel() {
        var reviewList = MutableLiveData<MutableList<Review>>()

    fun getAllReview(id:String) = viewModelScope.launch {
        var result:MutableList<Review>?=null
        withContext(IO){
            val response = productRepository.getProductById(id)
            if (response.success==true){
                result=response.singleProductData?.reviews
            }
        }
        reviewList.value=result!!

    }
//    var reviewList = liveData(IO) {
//        val response = productRepository.getProductById(id)
//        var result: MutableList<Review>? = null
//        if (response.success == true) {
//            result = response.singleProductData?.reviews
//        }
//        emit(result!!)
//    }

}

