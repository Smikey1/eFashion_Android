package com.hdd.globalmovie.domain.adapter

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.Review
import com.hdd.globalmovie.data.remoteDataSource.ServiceBuilder
import com.hdd.globalmovie.domain.viewModel.rating.RatingVM
import com.hdd.globalmovie.domain.viewModel.rating.RatingVMF
import com.hdd.globalmovie.repository.RatingRepository
import com.hdd.globalmovie.repository.ReviewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ReviewAdapter(private val reviewList: MutableList<Review>,private val reviewRating:Int):RecyclerView.Adapter<ReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.review_item_layout,parent,false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        Glide.with(holder.itemView.context).load(review.userId!!.profilePicUrl).circleCrop().into(holder.ril_Profile)
        holder.ril_fullname.text = review.userId.fullname
        holder.ril_review.text = review.review

        Toast.makeText(holder.itemView.context, "$reviewRating xa hai pahilako", Toast.LENGTH_SHORT).show()
        setRating(holder,review.rating-1)
        for (starValue in 0 until holder.reviewRatingContainer.childCount) {
            val starPosition:Int=starValue
            holder.reviewRatingContainer.getChildAt(starValue).setOnClickListener {
                setRating(holder,starPosition)
            }
        }

        holder.itemView.setOnLongClickListener {
            if(ServiceBuilder.uid == review.userId._id) {
                val reviewDialog = Dialog(holder.itemView.context)
                reviewDialog.setContentView(R.layout.edit_review_layout)
                reviewDialog.window?.setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val writeReview: EditText = reviewDialog.findViewById(R.id.arl_writeReview)
                writeReview.setText(review.review)
                val update: Button = reviewDialog.findViewById(R.id.arl_update)
                val delete: Button = reviewDialog.findViewById(R.id.arl_delete)

                update.setOnClickListener {
                    holder.ril_review.text =writeReview.text.toString()
                    CoroutineScope(IO).launch {
                        try {
                            val reviewRepository=ReviewRepository()
                            val response = reviewRepository.updateReview(review._id,Review(review = writeReview.text.toString()))
                            if (response.success==true){
                                withContext(Main){
                                    holder.ril_review.text =writeReview.text.toString()
                                    notifyDataSetChanged()
                                    Toast.makeText(holder.itemView.context, "Review Updated", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (ex:Exception){
                            print(ex)
                        }
                    }
                    reviewDialog.dismiss()
                }
                delete.setOnClickListener {
                    CoroutineScope(IO).launch {
                        try {
                            val reviewRepository=ReviewRepository()
                            val response = reviewRepository.deleteReview(review._id)
                            if (response.success==true){
                                withContext(Main){
                                    Toast.makeText(holder.itemView.context, "Review Deleted", Toast.LENGTH_SHORT).show()
                                    reviewList.remove(review)
                                    notifyDataSetChanged()
                                }
                            }
                        } catch (ex:Exception){
                            print(ex)
                        }
                    }
                    reviewDialog.dismiss()
                }
                reviewDialog.show()
            } else{
                Toast.makeText(it.context, "This is not your review", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun setRating(holder:ReviewViewHolder,starPosition: Int) {
        for (starValue in 0 until holder.reviewRatingContainer.childCount) {
            val starOnClick:ImageView=holder.reviewRatingContainer.getChildAt(starValue) as ImageView
            starOnClick.imageTintList= ColorStateList.valueOf(holder.itemView.context.getColor(R.color.starNotSelectedColor))
            if (starValue<=starPosition){
                starOnClick.imageTintList= ColorStateList.valueOf(holder.itemView.context.resources.getColor(R.color.starSelectedColor))
            }
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}

class ReviewViewHolder(view: View) :RecyclerView.ViewHolder(view){

    val ril_Profile:ImageView=view.findViewById(R.id.ril_Profile)
    val ril_fullname:TextView=view.findViewById(R.id.ril_fullname)
    val ril_review:TextView=view.findViewById(R.id.ril_review)
    val reviewRatingContainer: LinearLayout = view.findViewById(R.id.reviewRatingContainer)

}
