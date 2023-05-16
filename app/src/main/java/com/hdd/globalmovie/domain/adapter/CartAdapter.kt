package com.hdd.globalmovie.domain.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.CartItem
import com.hdd.globalmovie.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(
    private val cartItemList: MutableList<CartItem>,
) :
    RecyclerView.Adapter<CartAdapter.CIViewHolder>() {

    companion object{
        var total:Number= 0
        var selectedQuantity:Int=1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CIViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cilView = inflater.inflate(R.layout.cart_item_layout, parent, false)
        return CIViewHolder(cilView)
    }

    override fun onBindViewHolder(holder: CIViewHolder, position: Int) {
        val cart = cartItemList[position]
        Glide.with(holder.itemView.context).load(cart.productId?.productImageUrlList!![0]).into(holder.cil_product_image)

        if (cart.productId.freeCoupons > 0) {
            holder.cil_tv_free_coupon.visibility = VISIBLE
            holder.cil_free_coupon_img.visibility = VISIBLE
            if (cart.productId.freeCoupons == 1) {
                holder.cil_tv_free_coupon.text = " Free ${cart.productId.freeCoupons} coupon"
            } else {
                holder.cil_tv_free_coupon.text = " Free ${cart.productId.freeCoupons} coupons"
            }
        } else {
            holder.cil_tv_free_coupon.visibility = INVISIBLE
            holder.cil_free_coupon_img.visibility = INVISIBLE
        }

        holder.cil_product_name.text = cart.productId.productName
        holder.cil_product_price.text = "Rs. ${cart.productId.productPrice}"
        holder.cil_cutted_price.text = "Rs. ${cart.productId.productCuttedPrice}"
        holder.cil_quantity.text = "Qty: ${cart.quantity}"

        // quantity of cart
        holder.cil_quantity.setOnClickListener {
            val quantityDialog: Dialog = Dialog(it.context)
            quantityDialog.setContentView(R.layout.quantity_dialog)
            quantityDialog.setCancelable(false)
            quantityDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            val qd_cancel:Button = quantityDialog.findViewById(R.id.qd_cancel)
            val qd_ok:Button = quantityDialog.findViewById(R.id.qd_ok)
            val qtyNumber:EditText = quantityDialog.findViewById(R.id.qtyNumber)

            qd_cancel.setOnClickListener {
                quantityDialog.dismiss()
            }
            qd_ok.setOnClickListener {
                selectedQuantity = qtyNumber.text.toString().toInt()
                holder.cil_quantity.text = "Qty: ${qtyNumber.text}"
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val cartRepository= CartRepository()
                        val response=cartRepository.updateQuantity(cart.productId._id!!,qtyNumber.text.toString().toInt())
                        if(response.success==true){
                            withContext(Main){
                                holder.cil_quantity.text = "Qty: $qtyNumber"
                                notifyDataSetChanged()
                            }
                        }
                    }catch (ex:Exception){
                        print(ex)
                    }
                }
                quantityDialog.dismiss()
            }
            quantityDialog.show()
        }

        // delete cart item
        holder.cil_deleteItem.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val cartRepository= CartRepository()
                    val response=cartRepository.deleteProductFromCart(cart.productId._id!!)
                    if (response.success==true) {
                        withContext(Main) {
                            Toast.makeText(it.context, "Item removed", Toast.LENGTH_SHORT).show()
                            cartItemList.remove(cart)
                            notifyDataSetChanged()
                        }
                    }
                }catch (ex:Exception){
                    print(ex)
                }
            }
        }
        if (cart.productId.offerApplied > 0) {
            holder.cil_offer_applied.visibility = VISIBLE
            holder.cil_offer_applied.text = " ${cart.productId.offerApplied}% Offer Applied"
        } else {
            holder.cil_offer_applied.visibility = INVISIBLE
        }

        val totalPrice = cartItemList.map {
            it.productId!!.productPrice!!.toDouble().times(it.quantity.toDouble())
        }.sum()
        total = totalPrice.toInt()
    }


    override fun getItemCount(): Int {
        return cartItemList.size
    }

    class CIViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cil_product_image: ImageView = view.findViewById(R.id.cil_product_image)
        val cil_product_name: TextView = view.findViewById(R.id.cil_product_name)
        val cil_tv_free_coupon: TextView = view.findViewById(R.id.cil_tv_free_coupon)
        val cil_free_coupon_img: ImageView = view.findViewById(R.id.cil_free_coupon_img)
        val cil_product_price: TextView = view.findViewById(R.id.cil_product_price)
        val cil_cutted_price: TextView =view.findViewById(R.id.cil_cutted_price)
        val cil_quantity: TextView = view.findViewById(R.id.cil_quantity)
        val cil_offer_applied: TextView = view.findViewById(R.id.cil_offer_applied)
        val cil_deleteItem: TextView = view.findViewById(R.id.cil_deleteItem)
    }

}


