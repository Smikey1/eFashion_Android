package com.example.efashionwearos.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.efashionwearos.R
import com.example.efashionwearos.models.Order


class WearOrderAdapter(private val orderList: List<Order>): RecyclerView.Adapter<OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.ordel_list_layout,parent,false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)

    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val wear_order_id: TextView =view.findViewById(R.id.oll_bill_number)
    private val wear_product_image: ImageView =view.findViewById(R.id.oll_product_image)
    private val wear_order_indicator: ImageView =view.findViewById(R.id.oll_order_indicator)
    private val wear_product_name: TextView =view.findViewById(R.id.oll_product_name)
    private val wear_order_delivery_date: TextView = view.findViewById(R.id.oll_order_delivery_date)

    fun bind(order: Order) {
        for(item in order.order){
            wear_order_id.text = "Order No: ${item._id}"
            Glide.with(itemView.context).asBitmap().load(item.productId.productImageUrlList!![0])
                .into(object : BitmapImageViewTarget(wear_product_image) {
                    override fun setResource(resource: Bitmap?) {
                        wear_product_image.setImageBitmap(resource)
                        super.setResource(resource)
                    }
                })
//            Glide.with(context).load(item.productId.productImageUrlList!![0]).into(wear_product_image)
            wear_product_name.text = item.productId.productName

            when (order.deliveryStatusMessage) {
                "Pending" -> {wear_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.starSelectedColor))}
                "Cancelled" -> {wear_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorPrimary))}
                "Completed" -> { wear_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.teal_200))}
                else -> { wear_order_indicator.setColorFilter(ContextCompat.getColor(itemView.context, R.color.starNotSelectedColor))}
            }
            wear_order_delivery_date.text = order.deliveryStatusMessage
        }
    }

}