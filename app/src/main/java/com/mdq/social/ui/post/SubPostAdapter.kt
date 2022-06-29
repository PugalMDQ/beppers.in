package com.mdq.social.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mdq.social.R
import com.mdq.social.app.data.response.commentdetails.SubcommentsItem
import com.mdq.social.databinding.ItemSubCommentsBinding

class SubPostAdapter(val context:Context):RecyclerView.Adapter<SubPostAdapter.SubHolder>() {

    private var commentsItem: List<SubcommentsItem>? = null

    init {
        commentsItem = ArrayList()
    }

    fun setSubPostAdapter(itemList: List<SubcommentsItem>?) {
        if (itemList == null) {
            return
        }
        commentsItem = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubHolder {
        return SubHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_sub_comments, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SubHolder, position: Int) {
        val binding: ItemSubCommentsBinding = holder.getBinding()

        binding.textView94.setText("Nice hairCut")
    }

    override fun getItemCount(): Int {
        return 2
    }

    inner class SubHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        private var binding: ItemSubCommentsBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun getBinding(): ItemSubCommentsBinding {
            return binding!!
        }

    }

    private fun setImage(binding: ItemSubCommentsBinding, item: SubcommentsItem) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.leeminho)
            .error(R.drawable.leeminho)
            .transform(RoundedCorners(100))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context)
            .load(item.profilePicture)
            .apply(options)
            .into(binding.imageView28)

    }
}