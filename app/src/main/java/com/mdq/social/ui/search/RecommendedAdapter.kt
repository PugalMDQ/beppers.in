package com.mdq.social.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mdq.social.R
import com.mdq.social.app.data.response.useralbum.RecommendedItem
import com.mdq.social.databinding.ItemRecommendedBinding

class RecommendedAdapter(val context: Context, var recommendClickManager: RecommendClickManager):RecyclerView.Adapter<RecommendedAdapter.RecHolder>() {
    private var dataItem: List<RecommendedItem>? = null

    init {
        dataItem = ArrayList()
    }

    fun setRecommendedAdapter(itemList: List<RecommendedItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()
    }

    interface RecommendClickManager {
        fun onItemLickClick(position: Int, imageView32: ImageView, textView96: TextView, get: RecommendedItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        return RecHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recommended,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataItem!!.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {

        val item: RecommendedItem = dataItem!!.get(position)
        val binding: ItemRecommendedBinding = holder.getBinding()
        if (item.like == 1) {
            binding.imageView32.setImageResource(R.drawable.ic_heart)
        } else {
            binding.imageView32.setImageResource(R.drawable.ic_empty_heart)
        }

        binding.imageView32.setOnClickListener {
            recommendClickManager.onItemLickClick(
                position,
                binding.imageView32,binding.textView96,
                dataItem?.get(position)!!
            )
        }

        setImage(binding, item)
        setProfile(binding, item)
        binding.dataItem = item
    }



    private fun setProfile(binding: ItemRecommendedBinding, item: RecommendedItem) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_no_pictures)
            .error(R.drawable.ic_no_pictures)
            .transform(RoundedCorners(100))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context)
            .load(item.profilePicture)
            .apply(options)
            .into(binding.imageView28)
    }

    private fun setImage(binding: ItemRecommendedBinding, item: RecommendedItem) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_no_pictures)
            .error(R.drawable.ic_no_pictures)
            .transform(RoundedCorners(100))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context)
            .load(item.path)
            .apply(options)
            .into(binding.imageView31)
    }

    inner class RecHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemRecommendedBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)

        }

        fun getBinding(): ItemRecommendedBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }


    }
}