package com.mdq.social.ui.addpost

import android.net.Uri

import com.smarteist.autoimageslider.SliderViewAdapter

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import java.lang.Exception
import kotlin.collections.ArrayList


class SliderAdapter( var uri: ArrayList<Uri>,var clickManager:ClickManager) : SliderViewAdapter<SliderAdapter.Holder>() {

    interface ClickManager {
        fun onItemLickClick(position: Int)}

    override fun onCreateViewHolder(parent: ViewGroup): Holder {

        val view = LayoutInflater.from(parent.context)
            .inflate(com.mdq.social.R.layout.slider_item, parent, false)
        return Holder(view)

    }

    override fun onBindViewHolder(viewHolder: Holder, position: Int) {

        try {
            viewHolder.imageView?.setImageURI(uri.get(position))
            viewHolder.imageViewDelete?.setOnClickListener {
                clickManager.onItemLickClick(position)
                notifyDataSetChanged()
            }
        }catch (e:Exception){

        }


    }

    override fun getCount(): Int {
        return uri.size
    }

    inner class Holder(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView?=null
        var imageViewDelete: ImageView?=null

        init {
            imageView = itemView.findViewById(com.mdq.social.R.id.image_view)
            imageViewDelete = itemView.findViewById(com.mdq.social.R.id.deleteImage)
        }

    }
}
