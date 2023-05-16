package com.example.efashionwearos

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efashionwearos.adapter.WearCartAdapter
import com.example.efashionwearos.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : Activity() {
    private  lateinit var wearCartRV:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // #1: Cart Recycler view------------------>>>>>Start
        wearCartRV = findViewById<RecyclerView>(R.id.wearCartRV)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository= CartRepository()
                val response = repository.getAllCart()
                if (response.success==true){
                    val cartList = response.data!!
                    withContext(Dispatchers.Main){
                        wearCartRV.adapter = WearCartAdapter(cartList)
                        wearCartRV.layoutManager = LinearLayoutManager(this@CartActivity)
                    }
                }
            }catch (ex:Exception){

            }
        }
        // #1: Cart Recycler view------------------>>>>>End
    }
}