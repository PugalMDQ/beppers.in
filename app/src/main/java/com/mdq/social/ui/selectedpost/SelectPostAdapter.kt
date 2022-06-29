package com.mdq.social.ui.selectedpost

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.databinding.ItemAddPostBinding
import java.util.ArrayList

class SelectPostAdapter(
    var imageUrls: ArrayList<SelectedPostItem>?,
    var context: Context,
    var clickManager: ClickManager
) : RecyclerView.Adapter<SelectPostAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_add_post,
                parent,
                false
            )
        )
    }

    public interface ClickManager {
        fun onItemClick(position: Int, selectedPostItem: SelectedPostItem)
    }

    override fun getItemCount(): Int {
        return imageUrls?.size!!
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {


        if (imageUrls?.get(position)?.isSelected!!) {
            holder.getBinding().imageView27.alpha = 0.3f
        } else {
            holder.getBinding().imageView27.alpha = 1.0f
        }
        var filePath="file://${imageUrls?.get(position)?.imgPath}"

        Glide.with(context).load(filePath)
            .into(holder.getBinding().imageView27)
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var binding: ItemAddPostBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemAddPostBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

            imageUrls?.get(absoluteAdapterPosition)?.let { clickManager.onItemClick(absoluteAdapterPosition, it) }
        }
    }

}