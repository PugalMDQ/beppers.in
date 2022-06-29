package com.mdq.social.ui.profile

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.user_profile.DataItem
import com.mdq.social.databinding.ItemProfileBinding
import com.mdq.social.ui.home.SliderAdapter2


class ProfileAdapter(var context: Context, var galleryItem: List<DataItem>,var clickManager:ClickManager) :
    RecyclerView.Adapter<ProfileAdapter.ProHolder>() {
    private var productsItem: MutableList<String>? = mutableListOf()

    interface ClickManager {

        fun onItemLickClicksOfProfile(id:String,gallery:String,user_id:String,position:Int)

    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProHolder {
        return ProHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_profile,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProHolder, position: Int) {

        if (galleryItem.get(position).gallery?.contains(".mp4")!!) {



            holder.img!!.visibility = View.GONE
            holder.getBinding().player1.visibility = View.VISIBLE
            holder.getBinding().player1.setVideoURI(
                Uri.parse(
                    "https://mdqualityapps.in/gallery/" + galleryItem.get(
                        position
                    ).gallery
                )
            )
            holder.getBinding().player1.seekTo(1)
        }

        else {
            holder.getBinding().player1.visibility = View.GONE
            holder.img!!.visibility = View.VISIBLE
            var sliderAdapter: SliderAdapter2? = null
            var uri: ArrayList<Uri>? = ArrayList()
            if (galleryItem.get(position).gallery?.contains(",")!!) {
                val arr: List<String> = galleryItem.get(position).gallery?.split(",")!!
                for (i in 0 until arr.size) {
                    uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + arr.get(i)))
                }
                Glide.with(context).load(uri?.get(0)).into(holder.getBinding().imageslider101)
            } else {
                uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + galleryItem.get(position).gallery))
                Glide.with(context).load(uri?.get(0)).into(holder.getBinding().imageslider101)

            }
      }
        holder.videoView?.setOnClickListener {
            clickManager.onItemLickClicksOfProfile(galleryItem.get(position).id!!,
                galleryItem.get(position).gallery!!,
                galleryItem.get(position).user_id!!,position
            )
        }
        holder.img?.setOnClickListener {
            clickManager.onItemLickClicksOfProfile(galleryItem.get(position).id!!,
                galleryItem.get(position).gallery!!,
                galleryItem.get(position).user_id!!,position)
        }

    }

    override fun getItemCount(): Int {
        return galleryItem.size
    }

    inner class ProHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
       {
        private var binding: ItemProfileBinding? = null
        var img: ImageView?=null
        var videoView: VideoView?=null
        init {
            binding = DataBindingUtil.bind(itemView)
            binding = DataBindingUtil.bind(itemView)
            img=itemView.findViewById(R.id.imageslider101)
            videoView=itemView.findViewById(R.id.player1)
        }

        fun getBinding(): ItemProfileBinding {
            return binding!!
        }

    }
}