package com.hdd.globalmovie.domain.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hdd.globalmovie.presentation.activity.ProductDetailsActivity
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.WishlistItem
import com.hdd.globalmovie.repository.WishlistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class WishlistAdapter(
    private val wishlistList: MutableList<WishlistItem>,
    private val isFromWishlist: Boolean
) :
    RecyclerView.Adapter<WishlistViewHolder>(){
    var countryFilterList = mutableListOf<WishlistItem>()

    init {
        countryFilterList = wishlistList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wishlist_item_layout, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val wishlist = wishlistList[position]
        Glide.with(holder.itemView.context).load(wishlist.productId?.productImageUrlList!![0])
            .into(holder.wl_product_image)
        holder.wl_product_name.text = wishlist.productId.productName
        holder.wl_product_price.text = "Rs.${wishlist.productId.productPrice}"
        holder.wl_cutted_price.text = "Rs.${wishlist.productId.productCuttedPrice}"
        holder.wl_product_rating_text.text = wishlist.productId.productRating.toString()
        holder.wl_total_rating_text.text = "Total rating (${wishlist.productId.totalRating})"
        holder.wl_payment_way.text = wishlist.productId.paymentMethod
        if (wishlist.productId.freeCoupons > 0) {
            holder.wl_tv_free_coupon.visibility = View.VISIBLE
            holder.wl_free_coupon_img.visibility = View.VISIBLE
            if (wishlist.productId.freeCoupons == 1) {
                holder.wl_tv_free_coupon.text = " Free ${wishlist.productId.freeCoupons} coupon"
            } else {
                holder.wl_tv_free_coupon.text = " Free ${wishlist.productId.freeCoupons} coupons"
            }
        } else {
            holder.wl_tv_free_coupon.visibility = View.INVISIBLE
            holder.wl_free_coupon_img.visibility = View.INVISIBLE
        }
        holder.wl_delete.visibility = View.INVISIBLE
        if (isFromWishlist) {
            holder.wl_delete.visibility = View.VISIBLE
            holder.wl_delete.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val wishlistRepository = WishlistRepository()
                        val response =
                            wishlistRepository.deleteProductFromWishlist(wishlist.productId._id!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(it.context, "Item removed", Toast.LENGTH_SHORT)
                                    .show()
                                wishlistList.remove(wishlist)
                                notifyDataSetChanged()
                            }
                        }
                    } catch (ex: Exception) {
                        print(ex)
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ProductDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productId", wishlist.productId._id)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return wishlistList.size
    }
}

class WishlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val wl_product_image: ImageView = view.findViewById(R.id.wl_product_image)
    val wl_product_name: TextView = view.findViewById(R.id.wl_product_name)
    val wl_delete: ImageButton = view.findViewById(R.id.wl_delete)
    val wl_payment_way: TextView = view.findViewById(R.id.wl_payment_way)
    val wl_tv_free_coupon: TextView = view.findViewById(R.id.wl_tv_free_coupon)
    val wl_free_coupon_img: ImageView = view.findViewById(R.id.wl_free_coupon_img)
    val wl_product_rating_text: TextView = view.findViewById(R.id.wl_product_rating_text)
    val wl_total_rating_text: TextView = view.findViewById(R.id.wl_total_rating_text)
    val wl_product_price: TextView = view.findViewById(R.id.wl_product_price)
    val wl_cutted_price: TextView = view.findViewById(R.id.wl_cutted_price)

}