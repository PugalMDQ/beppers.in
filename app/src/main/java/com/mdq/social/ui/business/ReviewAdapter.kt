package com.mdq.social.ui.business

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.reviewresponse.DataItem
import com.mdq.social.databinding.ItemReviewBinding
import com.mdq.social.utils.DateUtils

class ReviewAdapter(val context: Context, var reviewList: List<DataItem?>?) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_review,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {


        val l: Long? =reviewList!!.get(position)?.created_at?.toLong()


        holder.getBinding().textView76.setText(reviewList!!.get(position)?.user_name)
            holder.getBinding().textView79.setText(reviewList!!.get(position)?.reviews)
            holder.getBinding().time.setText(DateUtils.getFormattedTime(l!!))


            if(reviewList!!.get(position)?.profile_photo!=null){
                Glide.with(context).load("http://mdqualityapps.in/profile/"+reviewList!!.get(position)?.profile_photo).into(holder.getBinding().imageView52)
            }else{
                holder.getBinding().imageView52.setImageDrawable(context.resources.getDrawable(R.drawable.user))
            }
         if(reviewList!!.get(position)?.rating?.toString()?.trim().equals("0")) {

        }
         if(reviewList!!.get(position)?.rating?.toString()?.trim().equals("1")){
            holder.getBinding().Img1.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
         }
        if(reviewList!!.get(position)?.rating?.toString()?.trim().equals("2")){
            holder.getBinding().Img1.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img2.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
        }
        if(reviewList!!.get(position)?.rating?.toString()?.trim().equals("3")){
            holder.getBinding().Img1.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img2.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img3.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
        }
        if(reviewList!!.get(position)?.rating?.toString()?.trim().equals("4")){
            holder.getBinding().Img1.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img2.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img3.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img4.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
        }
        if(reviewList!!.get(position)?.rating?.toString()?.trim().equals("5")){
            holder.getBinding().Img1.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img2.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img3.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img4.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
            holder.getBinding().Img5.setBackground(context.resources.getDrawable(R.drawable.ic_baseline_star_24))
        }
    }

    override fun getItemCount(): Int {
        return reviewList!!.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemReviewBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemReviewBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}