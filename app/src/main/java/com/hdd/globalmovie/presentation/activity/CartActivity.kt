package com.hdd.globalmovie.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.domain.adapter.CartAdapter
import com.hdd.globalmovie.data.models.ModelType
import com.hdd.globalmovie.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var cartRecycleView: RecyclerView
    private val CIL_TYPE:Int=0
    private val CTAL_TYPE:Int=1

    private val cartModelType = arrayListOf<ModelType>(
        ModelType(type = CIL_TYPE),
        ModelType(type = CIL_TYPE),
        ModelType(type = CIL_TYPE),
        ModelType(type = CIL_TYPE),
        ModelType(type = CTAL_TYPE)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // #1: Cart Recycler view------------------>>>>>Start
        cartRecycleView = findViewById<RecyclerView>(R.id.cartRecycleViewActivity)
        cartRecycleView.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cartRepository= CartRepository()
                val cartResponse=cartRepository.getAllCart()
                if(cartResponse.success==true){
                    val cartItemList=cartResponse.data!!
                    withContext(Dispatchers.Main){
                        val adapter = CartAdapter(cartItemList)
                        cartRecycleView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }catch (ex:Exception){
                print(ex)
            }
        }
        // #1: Cart Recycler view------------------>>>>>End
    }

}