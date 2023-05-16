package com.example.efashionwearos.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.efashionwearos.R
import com.example.efashionwearos.models.CartItem

class WearCartAdapter(
    private val cartItemList: MutableList<CartItem>,
) :
    RecyclerView.Adapter<WearCartAdapter.CIViewHolder>() {

    companion object {
        var total: Number = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CIViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cilView = inflater.inflate(R.layout.cart_item_layout, parent, false)
        return CIViewHolder(cilView)
    }

    override fun onBindViewHolder(holder: CIViewHolder, position: Int) {
        val cart = cartItemList[position]
//        Glide.with(context).load(cart.productId?.productImageUrlList!![0]).into(holder.cil_product_image)

        Glide.with(holder.itemView.context).asBitmap().load(cart.productId?.productImageUrlList!![0])
            .into(object : BitmapImageViewTarget(holder.cil_product_image) {
                override fun setResource(resource: Bitmap?) {
                    holder.cil_product_image.setImageBitmap(resource)
                    super.setResource(resource)
                }
            })


        holder.cil_product_name.text = cart.productId.productName
        holder.cil_product_price.text = "Rs. ${cart.productId.productPrice}"
        holder.cil_cutted_price.text = "Rs. ${cart.productId.productCuttedPrice}"
        holder.cil_quantity.text = "Qty: ${cart.quantity}"


        val totalPrice = cartItemList.map {
            it.productId!!.productPrice!!.toDouble().times(it.quantity.toDouble())
        }.sum()
        total = totalPrice.toInt()
    }


    override fun getItemCount(): Int {
        return cartItemList.size
    }

    class CIViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cil_product_image: ImageView = view.findViewById(R.id.cll_product_image)
        val cil_product_name: TextView = view.findViewById(R.id.cll_product_name)
        val cil_product_price: TextView = view.findViewById(R.id.cll_product_price)
        val cil_cutted_price: TextView = view.findViewById(R.id.cll_cutted_price)
        val cil_quantity: TextView = view.findViewById(R.id.cll_quantity)
    }
}