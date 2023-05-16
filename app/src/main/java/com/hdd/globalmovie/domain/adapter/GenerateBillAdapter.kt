package com.hdd.globalmovie.domain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.OrderItem
import com.hdd.globalmovie.data.models.Product

class GenerateBillAdapter(
    private val orderItem: List<OrderItem>,
): RecyclerView.Adapter<GenerateBillAdapter.BillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bill_layout, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val item = orderItem[position]
        holder.tvProductName.text = item.productId.productName
        holder.tvProductPrice.text = "Rs. ${item.productId.productPrice}"
        holder.tvQty.text = item.qty.toString()

//        for(item in orderItem) {
//            val product = item.productId
////            holder.bind(orderItem, product)
//            holder.tvProductName.text = product.productName
//            holder.tvProductPrice.text = "Rs. ${product.productPrice}"
//            holder.tvQty.text = item.qty.toString()
//        }
//        val cartItem = Order[position]


    }

    override fun getItemCount(): Int {
        return orderItem.size
    }

    class BillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(order: OrderItem, product: Product) {
//            tvProductName.text = product.productName
//            tvProductPrice.text = "Rs. ${product.productPrice}"
//            tvQty.text = order.qty.toString()
        }

        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvQty: TextView = view.findViewById(R.id.tvQty)
        val tvProductPrice: TextView = view.findViewById(R.id.tvProductPrice)
    }
}