package com.hdd.globalmovie.domain.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Order
import com.hdd.globalmovie.data.models.Product

class OrderAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.order_item_layout, parent, false)
        return OrderViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
            holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val oi_order_id: TextView = view.findViewById(R.id.oil_bill_number)
    private val oi_product_image: ImageView = view.findViewById(R.id.oil_product_image)
    private val oi_order_indicator: ImageView = view.findViewById(R.id.oil_order_indicator)
    private val oi_product_name: TextView = view.findViewById(R.id.oil_product_name)
    private val oi_order_delivery_date: TextView = view.findViewById(R.id.oil_order_delivery_date)

    fun bind(order: Order) {
        for(item in order.order){
            oi_order_id.text = "Order No: ${item._id}"
            Glide.with(itemView.context).load(item.productId.productImageUrlList!![0]).into(oi_product_image)
            oi_product_name.text = item.productId.productName

            when (order.deliveryStatusMessage) {
                "Pending" -> {oi_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.starSelectedColor))}
                "Cancelled" -> {oi_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorPrimary))}
                "Completed" -> { oi_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.teal_200))}
                else -> { oi_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.starNotSelectedColor))}
            }
            oi_order_delivery_date.text = order.deliveryStatusMessage
        }
    }
}