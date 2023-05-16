package com.hdd.globalmovie.presentation.activity

import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
//import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.domain.adapter.GPLAdapter
import com.hdd.globalmovie.domain.adapter.WishlistAdapter
import com.hdd.globalmovie.databinding.ActivityViewAllBinding
import com.hdd.globalmovie.data.localDataSource.ProductDatabase
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.data.models.WishlistItem
import com.hdd.globalmovie.repository.ProductRepository
import com.hdd.globalmovie.domain.viewModel.product.ProductVM
import com.hdd.globalmovie.domain.viewModel.product.ProductVMF
import java.util.*
import kotlin.collections.ArrayList

class ViewAllActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewAllBinding
    private lateinit var productVM: ProductVM
    private lateinit var viewAllRecyclerview: RecyclerView
    private lateinit var viewMoreGridView: GridView
    private lateinit var adapter1: WishlistAdapter
    private lateinit var productList :MutableList<WishlistItem>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all)
        binding = ActivityViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val layoutCode = intent.getIntExtra("layout_code", -1)
        viewAllRecyclerview = findViewById(R.id.viewAllRecyclerview)
        viewMoreGridView = findViewById(R.id.viewMoreGridview)

        val productDAO = ProductDatabase.getInstance(this).productDao()
        val productRepository = ProductRepository(productDAO)
        val factory = ProductVMF(productRepository)
        productVM= ViewModelProvider(this,factory).get(ProductVM::class.java)

        // #1: View All Recycler view------------------>>>>>Start
        productVM.getAllProduct()
        productVM.productList.observe(this, Observer {
            if (layoutCode == 1) {
                // for Grid view
                supportActionBar?.title = "Trending Items"
                viewMoreGridView.visibility = View.VISIBLE
                viewAllRecyclerview.visibility = View.GONE
                viewMoreGridView.adapter =GPLAdapter(this@ViewAllActivity,it!!)
            } else{
                // for recycler view
                supportActionBar?.title = "Deals of the day"
                viewAllRecyclerview.visibility = View.VISIBLE
                viewMoreGridView.visibility = View.GONE
                viewAllRecyclerview.layoutManager = LinearLayoutManager(this@ViewAllActivity)
                productList=getWishlistList(it!!)
                adapter1=WishlistAdapter(productList, false)
                viewAllRecyclerview.adapter = adapter1
            }
        })
    }
    // #1: View All Recycler view------------------>>>>>End

    private fun getWishlistList(productList:MutableList<Product>):MutableList<WishlistItem> {
        val wishlist = mutableListOf<WishlistItem>()
        for (product in productList){
            wishlist.add(WishlistItem(productId = product))
        }
        return wishlist
    }

    private fun searchProduct(pattern:String){
        productVM.getProductByPattern(pattern)
        productVM.productSearchList.observe(this, Observer {
            adapter1=WishlistAdapter(getWishlistList(it!!), false)
            viewAllRecyclerview.adapter = adapter1
        })
    }

     override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.category_menu, menu)
         val search = menu.findItem(R.id.category_search)
         val searchView = search?.actionView as SearchView
         searchView.queryHint="Search Product Name"
         searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
             override fun onQueryTextSubmit(newText: String?): Boolean {
                 return false
             }

             override fun onQueryTextChange(newText: String?): Boolean {
                 if(newText!!.isNotEmpty()){
                 searchProduct(newText)
                 }
                 return true
             }
         })
        return true
    }

    override fun onOptionsItemSelected(selectedItem: MenuItem): Boolean {
        when (selectedItem.itemId) {
            // for back button action
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}