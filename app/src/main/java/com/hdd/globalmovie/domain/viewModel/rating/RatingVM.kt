package com.hdd.globalmovie.domain.viewModel.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hdd.globalmovie.data.models.Rating
import com.hdd.globalmovie.data.models.Review
import com.hdd.globalmovie.repository.ProductRepository
import com.hdd.globalmovie.repository.RatingRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RatingVM(private val ratingRepository: RatingRepository) : ViewModel() {
        var previousRating = MutableLiveData<Int>()

    fun getRatingByProduct(productId:String) = viewModelScope.launch {
        var result:Int?=null
        withContext(IO){
            val response = ratingRepository.getRatingByProduct(productId)
            if (response.success==true){
                result=response.previousRating!!
            }
        }
        previousRating.value=result!!

    }

}

