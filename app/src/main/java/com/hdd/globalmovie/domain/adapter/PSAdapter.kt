package com.hdd.globalmovie.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.ProductSpecificationDetails

class PSAdapter(
    private val productSpecificationList: List<ProductSpecificationDetails>,
    private val context: Context
):RecyclerView.Adapter<PSAdapter.ProductSpecificationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductSpecificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_specification_item_layout, parent, false)
        return ProductSpecificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductSpecificationViewHolder, position: Int) {
        val product = productSpecificationList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productSpecificationList.size
    }

    class ProductSpecificationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val featureName: TextView = view.findViewById(R.id.featureName)
        private val featureValue:TextView = view.findViewById(R.id.featureValue)
        fun bind(product: ProductSpecificationDetails) {
            featureName.text=product.featureName
            featureValue.text=product.featureValue
        }
    }

}