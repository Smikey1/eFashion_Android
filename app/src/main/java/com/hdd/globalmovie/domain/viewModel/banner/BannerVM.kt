package com.hdd.globalmovie.domain.viewModel.banner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hdd.globalmovie.data.models.Banner
import com.hdd.globalmovie.repository.BannerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BannerVM(private val bannerRepository: BannerRepository): ViewModel(){
    var bannerList = MutableLiveData<MutableList<SlideModel>>()
    fun getAllBanner(categoryName:String) = viewModelScope.launch {
        var result:MutableList<String>?=null
        val myList = mutableListOf<SlideModel>()
        withContext(Dispatchers.IO){
            val response = bannerRepository.getAllBanner(categoryName)
                result=response.data!!
            for (i in result!!){
                myList.add(SlideModel(i, ScaleTypes.CENTER_INSIDE))
            }
        }
        bannerList.value=myList

    }
    
}

