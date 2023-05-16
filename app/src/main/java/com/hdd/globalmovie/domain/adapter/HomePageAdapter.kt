package com.hdd.globalmovie.domain.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ActionTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.interfaces.TouchListener
import com.denzcoskun.imageslider.models.SlideModel
import com.hdd.globalmovie.R
import com.hdd.globalmovie.presentation.activity.ViewAllActivity
import com.hdd.globalmovie.data.models.ModelType
import com.hdd.globalmovie.data.models.Product

class HomePageAdapter (
    private val bannerList: MutableList<SlideModel>,
    private val productList: MutableList<Product>,
    private val productModelTypeList: List<ModelType>,
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        val modelType = productModelTypeList[position]
        return when (modelType.type) {
            0 -> {0}
            1 -> {1}
            2 -> {2}
            else -> {-1}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val sliderView = inflater.inflate(R.layout.ads_slider, parent, false)
                SliderViewHolder(sliderView)
            }
            1 -> {
                val hslView = inflater.inflate(R.layout.horizontal_scroll_layout, parent, false)
                HSLViewHolder(hslView)
            }
            2 -> {
                val gplView = inflater.inflate(R.layout.grid_product_layout, parent, false)
                GPLViewHolder(gplView)
            }
            else -> {
                val extraView = inflater.inflate(R.layout.catagory_item, parent, false)
                ExtraViewHolder(extraView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val myProduct = productList[position]
        when (holder) {
            is SliderViewHolder ->{holder.bind(bannerList)}
            is HSLViewHolder -> { holder.bind(productList) }
            is GPLViewHolder -> { holder.bind(productList) }
            else -> { throw IllegalArgumentException() }
        }
    }

    override fun getItemCount(): Int {
        return productModelTypeList.size
    }

    class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(bannerList: MutableList<SlideModel>) {

            val imageSlider: ImageSlider = itemView.findViewById(R.id.image_slider)
            val imageList = ArrayList<SlideModel>() // Create image list
            imageSlider.setImageList(bannerList)

            imageSlider.setItemClickListener(object : ItemClickListener {
                override fun onItemSelected(position: Int) {
                }
            })

            imageSlider.setTouchListener(object : TouchListener {
                override fun onTouched(touched: ActionTypes) {
                    if (touched == ActionTypes.DOWN) {
                        imageSlider.stopSliding()
                    } else if (touched == ActionTypes.UP) {
                        imageSlider.startSliding(1000)
                    }
                }
            })
        }
    }

    class HSLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val hslRecycleView:RecyclerView=view.findViewById(R.id.hsl_recyclerView)
        private val hsl_viewAll:Button= view.findViewById(R.id.hsl_viewAll)
        fun bind(productList:MutableList<Product>) {
            val recyclerViewPool= RecyclerView.RecycledViewPool()
            hslRecycleView.setRecycledViewPool(recyclerViewPool)
            hslRecycleView.layoutManager =LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            hslRecycleView.adapter = HSLAdapter(productList)
            hsl_viewAll.setOnClickListener {
                val viewAllIntent=Intent(itemView.context, ViewAllActivity::class.java)
                viewAllIntent.putExtra("layout_code",0)
                itemView.context.startActivity(viewAllIntent)
            }
        }
    }
    class GPLViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val gplGridView :GridView = view.findViewById(R.id.gplGridView)
//        val trendingItems :TextView = view.findViewById(R.id.gplTrendingItem)
private val viewMore:Button = view.findViewById(R.id.gplViewMore)

        fun bind(productLists:MutableList<Product>) {
            gplGridView.adapter = GPLAdapter(itemView.context, productLists)
            viewMore.setOnClickListener {
                val viewMoreIntent=Intent(itemView.context, ViewAllActivity::class.java)
                viewMoreIntent.putExtra("layout_code",1)
                itemView.context.startActivity(viewMoreIntent)
            }
        }

    }

    class ExtraViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}


