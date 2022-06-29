package com.mdq.social.ui.searchdetails

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItemes
import com.mdq.social.databinding.ItemGalleryAdapterBinding
import com.mdq.social.ui.home.SliderAdapter2

class VideoAdapter(var videoItem:  List<DataItemes>?, var context: Context,var post:postClick) : RecyclerView.Adapter<VideoAdapter.VidHolder>() {


    interface postClick{
        fun onItemLickClicksOfProfile( videoItem: List<DataItemes>?,position:Int)
        fun enter()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):VidHolder {
        return VidHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_gallery_adapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VidHolder, position: Int) {

        holder.itemView.setOnClickListener {
            post.onItemLickClicksOfProfile(videoItem,position)
        }

        holder.getBinding().muteAndUnmute?.setOnClickListener {
            if (holder.getBinding().muteAndUnmute?.getTag().equals("work")) {
                holder.getBinding().muteAndUnmute!!.setImageResource(R.drawable.ic_pause__1_)
                holder.getBinding().payer1!!.start()
                holder.getBinding().muteAndUnmute.setTag("notwork")
            }else {
                holder.getBinding().muteAndUnmute!!.setImageResource(R.drawable.ic_play__1_)
                holder.getBinding().payer1!!.pause()
                holder.getBinding().muteAndUnmute.setTag("work")
            }
        }
        if (videoItem?.get(position)?.gallery?.contains(".mp4")!!) {
            holder.getBinding().cardformuteAndUnmute!!.visibility=View.VISIBLE
            holder.getBinding().imageSlider!!.visibility=View.GONE
            holder.getBinding().payer1.visibility=View.VISIBLE
            holder.getBinding().payer1.setVideoURI(Uri.parse("https://mdqualityapps.in/gallery/"+videoItem?.get(position)?.gallery))
            holder.getBinding().payer1.seekTo(2)
        }
        else {
            holder.getBinding().cardformuteAndUnmute!!.visibility=View.GONE
            holder.getBinding().imageSlider!!.visibility=View.VISIBLE
            holder.getBinding().payer1.visibility=View.GONE
            var sliderAdapter: SliderAdapter2?=null
            var uri: ArrayList<Uri>?= ArrayList()
            if(videoItem?.get(position)!!.gallery!!.contains(",")) {
                val str = videoItem?.get(position)?.gallery
                val arr: List<String> = videoItem?.get(position)?.gallery!!.split(",")
                for(i in arr.indices)
                {
                    uri?.add(Uri.parse("https://mdqualityapps.in/gallery/"+arr.get(i)))
                }
            }else {
                uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + videoItem?.get(position)?.gallery))
            }
            Glide.with(context)
                .load(uri?.get(0))
                .into(holder.getBinding().imageSlider)
        }
    }

    override fun getItemCount(): Int {
        if(videoItem!=null) {
            return videoItem!!.size
        }else{
            return 0
        }
    }

    inner class VidHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var binding: ItemGalleryAdapterBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun getBinding(): ItemGalleryAdapterBinding {
            return binding!!
        }



    }

}