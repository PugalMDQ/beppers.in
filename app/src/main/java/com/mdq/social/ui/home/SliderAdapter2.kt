package com.mdq.social.ui.home

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter2(var context: Context, var uri: ArrayList<Uri>): SliderViewAdapter<SliderAdapter2.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.mdq.social.R.layout.slider_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: Holder, position: Int) {
        viewHolder.imageViewDelete?.visibility=View.GONE
        Glide.with(context).load(uri.get(position)).into(viewHolder.imageView!!)
    }

    override fun getCount(): Int {
        return uri.size
    }

    inner class Holder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView?=null
        var imageViewDelete: ImageView?=null

        init {
            imageView = itemView.findViewById(com.mdq.social.R.id.image_view)
            imageViewDelete = itemView.findViewById(com.mdq.social.R.id.deleteImage)
        }

    }

}