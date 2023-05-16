package com.hdd.globalmovie.presentation.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Order
import com.hdd.globalmovie.data.models.OrderItem
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.domain.adapter.CartAdapter
import com.hdd.globalmovie.databinding.FragmentCartBinding
import com.hdd.globalmovie.repository.CartRepository
import com.hdd.globalmovie.domain.viewModel.cart.CartVM
import com.hdd.globalmovie.domain.viewModel.cart.CartVMF
import com.hdd.globalmovie.presentation.activity.BillingActivity
import com.hdd.globalmovie.presentation.activity.CartActivity
import com.hdd.globalmovie.presentation.activity.OrderActivity
import com.hdd.globalmovie.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartVM: CartVM
    private val channelID = "com.hdd.globalMovie.notificationChannel1"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY= "key_reply1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelID,  "eFashion Store", "Notification Channel")

        val cartRepository= CartRepository()
        val factory = CartVMF(cartRepository)
        cartVM= ViewModelProvider(this,factory).get(CartVM::class.java)
        binding.lifecycleOwner=requireActivity()
        binding.myCartVM=cartVM
        cartVM.getAllCart()
        cartVM.cartList.observe(requireActivity(), Observer {
            binding.ctalTotalItems.text="Price (${it.size} items)"
            val adapter =CartAdapter(it)
            binding.cartRecycleView.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.cartRecycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        })

        cartVM.cart.observe(requireActivity(), Observer {
            val savedAmount=calculateSavedAmount(productCuttedPrice = it.productId?.productCuttedPrice!!,productPrice = it.productId.productPrice!!)
            binding.ctalSavedAmount.text="You have saved Rs. $savedAmount on this order"
        })

        binding.fmcContinue.setOnClickListener {
            displayNotification()
            cartVM.cartList.observe(requireActivity(), Observer {
                val orderItemList = arrayListOf<OrderItem>()
                for (cartItem in it){
                    orderItemList.add(OrderItem(qty = cartItem.quantity,productId = Product(_id=cartItem.productId!!._id)))
                }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository= OrderRepository()
                        val response = repository.addProductToOrder(Order(order = orderItemList))
                        if (response.success==true) {
                            withContext(Dispatchers.Main) {
                            }
                        }
                    }catch (ex: Exception){
                        print(ex)
                    }
                }
            })
        }
        return binding.root
    }
    private fun calculateSavedAmount(productCuttedPrice:Int,productPrice:Int) :Int{
        return productCuttedPrice-productPrice
    }

    @SuppressLint("RestrictedApi")
    private fun displayNotification() {
        val notificationId = 45
        val tapResultIntent = Intent(requireContext(), BillingActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            tapResultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // reply action
        val remoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Total amount to be paid is: ${cartVM.allCartPrice.value}")
            build()
        }
        val replyAction: NotificationCompat.Action=
            NotificationCompat.Action.Builder(0, "Make Payment",pendingIntent).addRemoteInput(remoteInput).build()

        //action button 1
        val intent2 = Intent(requireContext(), CartActivity::class.java)
        val pendingIntent2: PendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action2 : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"View Cart",pendingIntent2).build()
        // action button 2
        val intent3 = Intent(requireContext(), OrderActivity::class.java)
        val pendingIntent3: PendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent3,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val action3 : NotificationCompat.Action = NotificationCompat.Action.Builder(0,"View Order",pendingIntent3).build()

        val notification = NotificationCompat.Builder(requireContext(), channelID)
            .setContentTitle("eFashion Store")
            .setContentText("Make a payment to confirm your order")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(action2)
            .addAction(action3)
            .addAction(replyAction)
            .build()
        notificationManager?.notify(notificationId, notification)

    }

    private fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)

        }

    }


}