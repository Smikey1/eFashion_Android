package com.hdd.globalmovie.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hdd.globalmovie.R
import com.hdd.globalmovie.domain.adapter.CategoryAdapter
import com.hdd.globalmovie.domain.adapter.HomePageAdapter
import com.hdd.globalmovie.databinding.FragmentHomeBinding
import com.hdd.globalmovie.data.localDataSource.ProductDatabase
import com.hdd.globalmovie.data.models.Banner
import com.hdd.globalmovie.data.models.ModelType
import com.hdd.globalmovie.domain.viewModel.banner.BannerVM
import com.hdd.globalmovie.domain.viewModel.banner.BannerVMF
import com.hdd.globalmovie.repository.CategoryRepository
import com.hdd.globalmovie.repository.ProductRepository
import com.hdd.globalmovie.domain.viewModel.product.ProductVM
import com.hdd.globalmovie.domain.viewModel.product.ProductVMF
import com.hdd.globalmovie.repository.BannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var bannerVM: BannerVM
    private lateinit var bannerList:  MutableList<SlideModel>
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productVM: ProductVM
    private val BANNER_SLIDER_TYPE:Int=0
    private val HSL_TYPE:Int=1
    private val GPL_TYPE:Int=2

    private val productModelType = arrayListOf<ModelType>(
        ModelType(type = BANNER_SLIDER_TYPE),
        ModelType(type = HSL_TYPE),
        ModelType(type = GPL_TYPE),
        ModelType(type = HSL_TYPE),
        ModelType(type = GPL_TYPE),
        ModelType(type = HSL_TYPE),
        ModelType(type = GPL_TYPE),
        ModelType(type = HSL_TYPE),
        ModelType(type = GPL_TYPE),
        ModelType(type = HSL_TYPE)
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        val productDAO = ProductDatabase.getInstance(requireContext()).productDao()
        val productRepository = ProductRepository(productDAO)
        val factory = ProductVMF(productRepository)
        productVM= ViewModelProvider(this,factory).get(ProductVM::class.java)
        bannerVM= ViewModelProvider(this,BannerVMF(BannerRepository())).get(BannerVM::class.java)

        // #1: Category Recycler view------------------>>>>>Start
        binding.categoryRecycleView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        CoroutineScope(IO).launch {
            try {
                val categoryRepository = CategoryRepository()
                val response = categoryRepository.getAllCategory()
                if (response.success == true) {
                    val categoryList = response.data!!
                    withContext(Main) {
                        binding.categoryRecycleView.adapter = CategoryAdapter(categoryList)
                    }

                }
            } catch (ex: Exception) {
                print(ex)
            }
        }
        // #1: Category Recycler view------------------>>>>>End


        // #2: Multi Recycler view------------------>>>>>Start
        bannerVM.getAllBanner("Home")
        bannerVM.bannerList.observe(requireActivity(), Observer {
            bannerList=it
        })
        productVM.getAllProduct()
        productVM.productList.observe(requireActivity(),  Observer {
            binding.homePageRecycleView.adapter = HomePageAdapter(bannerList,it,productModelType)
            binding.homePageRecycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        })
        // #2: Multi Recycler view------------------>>>>>End

        return binding.root
    }

}
