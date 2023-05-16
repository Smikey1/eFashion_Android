package com.hdd.globalmovie.domain.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.hdd.globalmovie.presentation.activity.ProductDetailsActivity
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Product

class GPLAdapter(
    private val context: Context,
    private val productList: MutableList<Product>,
) : BaseAdapter() {

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        val view:View =View.inflate(context,R.layout.hsl_item, null)
        view.elevation=0F  // set for elevation
        view.setBackgroundColor(Color.WHITE)
            val hsl_productImage: ImageView = view.findViewById(R.id.hsl_productImage)
            val hsl_productTitle: TextView = view.findViewById(R.id.hsl_productTitle)
            val hsl_productPrice: TextView = view.findViewById(R.id.hsl_productPrice)
            val hsl_productDescription: TextView = view.findViewById(R.id.hsl_productDescription)

            val product = productList[position]
        if (product.productImageUrlList!!.isNotEmpty()){
            Glide.with(view.context).load(product.productImageUrlList!![0]).into(hsl_productImage)
        }
        hsl_productTitle.text = product.productName
            hsl_productDescription.text = product.productShortDescription
            hsl_productPrice.text = "Rs. ${product.productPrice}"
        view.setOnClickListener {
            val intent = Intent(view.context, ProductDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productId", product._id)
            intent.putExtras(bundle)
            ContextCompat.startActivity(view.context, intent, null)
        }
        return view
    }
}