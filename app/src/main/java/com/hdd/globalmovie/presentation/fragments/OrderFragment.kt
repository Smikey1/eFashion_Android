package com.hdd.globalmovie.presentation.fragments

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.domain.adapter.OrderAdapter
import com.hdd.globalmovie.data.models.OrderItem
import com.hdd.globalmovie.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderFragment : Fragment() {
    private lateinit var orderList: List<OrderItem>
    private lateinit var order_recyclerView: RecyclerView
    private var notificationManager: NotificationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_order, container, false)
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        receivedInput()
        // #1: Order Recycler view------------------>>>>>Start
        order_recyclerView = view.findViewById<RecyclerView>(R.id.order_recyclerView)
        CoroutineScope(IO).launch {
            try {
                val repository= OrderRepository()
                val response = repository.getAllOrder()
                if (response.success==true){
                    val orderDataList = response.data!!
                        withContext(Main){
                            order_recyclerView.adapter = OrderAdapter(orderDataList)
                            order_recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        }
                }
            }catch (ex:Exception){

            }
        }
        // #1: Order Recycler view------------------>>>>>End

        return view
    }

    private fun receivedInput(){
        val KEY_REPLY= "key_reply"
        val intent = requireActivity().intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        if (remoteInput!=null){
            val inputString =remoteInput.getCharSequence(KEY_REPLY).toString()
            Toast.makeText(requireContext(), "Received $remoteInput", Toast.LENGTH_SHORT).show()

            val channelID = "com.hdd.globalMovie.notificationChannel"
            val notificationId = 45
            val receivedNotification = NotificationCompat.Builder(requireContext(), channelID)
                .setContentTitle("Ordered Successful")
                .setContentText("Your payment received")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_HIGH).build()

            notificationManager?.notify(notificationId,receivedNotification)
        }
    }

}