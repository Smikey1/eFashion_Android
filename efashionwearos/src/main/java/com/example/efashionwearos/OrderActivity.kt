package com.example.efashionwearos

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.efashionwearos.adapter.WearOrderAdapter
import com.example.efashionwearos.models.Order
import com.example.efashionwearos.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderActivity : Activity() {
    private  lateinit var wear_recycler_view:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // #1: Order Recycler view------------------>>>>>Start
        wear_recycler_view = findViewById<RecyclerView>(R.id.wearOrderRV)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository= OrderRepository()
                val response = repository.getAllOrder()
                if (response.success==true){
                    val orderDataList = response.data!!
                    withContext(Dispatchers.Main){
                        wear_recycler_view.adapter = WearOrderAdapter(orderDataList)
                        wear_recycler_view.layoutManager = LinearLayoutManager(this@OrderActivity)
                    }
                }
            }catch (ex:Exception){

            }
        }
        // #1: Order Recycler view------------------>>>>>End
    }
}