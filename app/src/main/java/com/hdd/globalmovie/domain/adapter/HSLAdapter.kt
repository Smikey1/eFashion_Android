package com.hdd.globalmovie.domain.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hdd.globalmovie.presentation.activity.ProductDetailsActivity
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Product

class HSLAdapter(private val productList: MutableList<Product>):RecyclerView.Adapter<HSLAViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HSLAViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.hsl_item,parent,false)
        return HSLAViewHolder(view)
    }

    override fun onBindViewHolder(holder: HSLAViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}

class HSLAViewHolder(view: View) :RecyclerView.ViewHolder(view){

    private val hsl_productImage:ImageView=view.findViewById(R.id.hsl_productImage)
    private val hsl_productTitle:TextView=view.findViewById(R.id.hsl_productTitle)
    private val hsl_productPrice:TextView= view.findViewById(R.id.hsl_productPrice)
    private val hsl_productDescription:TextView=view.findViewById(R.id.hsl_productDescription)

    fun bind(product: Product) {
        hsl_productTitle.text = product.productName
        hsl_productDescription.text = product.productShortDescription
        hsl_productPrice.text = "Rs. ${product.productPrice}"
        if (product.productImageUrlList!!.isNotEmpty()){
            Glide.with(itemView.context).load(product.productImageUrlList!![0]).into(hsl_productImage)
        }
        itemView.setOnClickListener {
            val intent = Intent(itemView.context, ProductDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productId", product._id)
            intent.putExtras(bundle)
            ContextCompat.startActivity(itemView.context, intent, null)
        }
    }
}
