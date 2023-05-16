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
import com.hdd.globalmovie.presentation.activity.CategoryActivity
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Category

class CategoryAdapter(private val categoryList: List<Category>) :
    RecyclerView.Adapter<CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.catagory_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        holder.bind(categoryItem)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}

class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val category_name = view.findViewById<TextView>(R.id.category_name)
    val category_icon = view.findViewById<ImageView>(R.id.category_icon)

    fun bind(categoryItem: Category) {
        category_name.text = categoryItem.categoryName
        Glide.with(itemView.context).load(categoryItem.categoryImageUrl).into(category_icon)

        itemView.setOnClickListener {
            val categoryName: String = categoryItem.categoryName!!
            if (adapterPosition != 0) {
                val intent = Intent(itemView.context, CategoryActivity::class.java)
                //Create the bundle
                val bundle = Bundle()
                //A dd your data from getFactualResults method to bundle
                bundle.putString("categoryName", categoryName)
                //Add the bundle to the intent
                intent.putExtras(bundle)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}