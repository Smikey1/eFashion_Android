package com.hdd.globalmovie.presentation.activity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderActivity : AppCompatActivity() {
    private lateinit var order_recyclerView: RecyclerView
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        receivedInput()
        // #1: Order Recycler view------------------>>>>>Start
        order_recyclerView = findViewById<RecyclerView>(R.id.order_recyclerView_activity)
        order_recyclerView.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository= OrderRepository()
                val response = repository.getAllOrder()
                if (response.success==true){
                    val orderDataList = response.data!!
                    withContext(Dispatchers.Main){
//                            order_recyclerView.adapter = OrderAdapter(orderDataList)
                    }

                }
            }catch (ex:Exception){

            }
        }
    }

//    private fun receivedInput(){
//        val KEY_REPLY= "key_reply"
//        val intent = this.intent
//        val remoteInput = RemoteInput.getResultsFromIntent(intent)
//        if (remoteInput!=null){
//            val inputString =remoteInput.getCharSequence(KEY_REPLY).toString()
////            Toast.makeText(this, "Received $remoteInput", Toast.LENGTH_SHORT).show()
//            val channelID = "com.hdd.globalMovie.notificationChannel"
//            val notificationId = 45
//            val receivedNotification = NotificationCompat.Builder(this, channelID)
//                .setContentTitle("Ordered Successful")
//                .setContentText("Your payment received")
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setPriority(NotificationCompat.PRIORITY_HIGH).build()
//
//            notificationManager?.notify(notificationId,receivedNotification)
//        }
//    }
}