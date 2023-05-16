package com.hdd.globalmovie.presentation.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.localDataSource.ProductDatabase
import com.hdd.globalmovie.data.models.ModelType
import com.hdd.globalmovie.databinding.ActivityCategoryBinding
import com.hdd.globalmovie.domain.adapter.HomePageAdapter
import com.hdd.globalmovie.domain.viewModel.banner.BannerVM
import com.hdd.globalmovie.domain.viewModel.banner.BannerVMF
import com.hdd.globalmovie.domain.viewModel.product.ProductVM
import com.hdd.globalmovie.domain.viewModel.product.ProductVMF
import com.hdd.globalmovie.repository.BannerRepository
import com.hdd.globalmovie.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var productVM: ProductVM
    private lateinit var bannerVM: BannerVM
    private lateinit var bannerList: MutableList<SlideModel>
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
        ModelType(type = HSL_TYPE)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        // setDisplayHomeAsUpEnabled(true) --> create back button in toolbar
        val bundle = intent.extras
        //Extract the data…
        val categoryTitle = bundle!!.getString("categoryName")
        Toast.makeText(this, "$categoryTitle", Toast.LENGTH_SHORT).show()
        supportActionBar?.title = categoryTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val productDAO = ProductDatabase.getInstance(this).productDao()
        val productRepository = ProductRepository(productDAO)
        val factory = ProductVMF(productRepository)
        productVM= ViewModelProvider(this,factory).get(ProductVM::class.java)
        bannerVM= ViewModelProvider(this, BannerVMF(BannerRepository())).get(BannerVM::class.java)


        // #2: Multi Recycler view------------------>>>>>Start
        bannerVM.getAllBanner(categoryTitle!!)
        bannerVM.bannerList.observe(this, Observer {
            bannerList=it
        })
        productVM.getProductByCategory(categoryTitle)
        productVM.productCategoryList.observe(this,  Observer {
            binding.categoryRecyclerView.adapter = HomePageAdapter(bannerList,it,productModelType)
            binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        })
        // #2: Multi Recycler view------------------>>>>>End
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.category_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(selectedItem: MenuItem): Boolean {
        when (selectedItem.itemId) {
            R.id.category_search -> {
                Toast.makeText(this, "Nav Store clicked", Toast.LENGTH_SHORT).show()
            }
            // for back button action
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}