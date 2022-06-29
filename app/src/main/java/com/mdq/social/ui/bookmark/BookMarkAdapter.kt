package com.mdq.social.ui.bookmark

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.bookmarklist.BookmarkListItem
import com.mdq.social.app.data.response.connectionrequest.dateItems
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItemes
import com.mdq.social.databinding.ItemBookmarkBinding
import com.mdq.social.ui.adapters.AdapterForSearchPost
import com.mdq.social.ui.home.SliderAdapter2
import com.mdq.social.ui.profile.ProfileAdapter
import com.mdq.social.utils.UiUtils.showToast
import java.lang.reflect.Array.get

class BookMarkAdapter(var context: Context,var dataItem:List<BookmarkListItem>,var clickManager: ClickManager):RecyclerView.Adapter<BookMarkAdapter.BookHolder>() {

    interface ClickManager {
        fun onItemLickClicksOfProfile(id:String,gallery:String,user_id:String,position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_bookmark,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val binding: ItemBookmarkBinding = holder.getBinding()

        if (dataItem.get(position).gallery?.contains(".mp4")!!) {

            holder.img!!.visibility = View.GONE
            holder.getBinding().player1.visibility = View.VISIBLE
            holder.getBinding().player1.setVideoURI(
                Uri.parse(
                    "https://mdqualityapps.in/gallery/" + dataItem.get(
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
            if (dataItem.get(position).gallery?.contains(",")!!) {
                val arr: List<String> = dataItem.get(position).gallery?.split(",")!!
                for (i in 0 until arr.size) {
                    uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + arr.get(i)))
                }
                Glide.with(context).load(uri?.get(0)).into(holder.getBinding().imageslider101)
            } else {
                uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + dataItem.get(position).gallery))
                Glide.with(context).load(uri?.get(0)).into(holder.getBinding().imageslider101)

            }
        }
        holder.videoView?.setOnClickListener {
            clickManager.onItemLickClicksOfProfile(dataItem.get(position).id!!,
                dataItem.get(position).gallery!!,
                dataItem.get(position).user_id!!,position
            )
        }

        holder.img?.setOnClickListener {
            clickManager.onItemLickClicksOfProfile(dataItem.get(position).id!!,
                dataItem.get(position).gallery!!,
                dataItem.get(position).user_id!!,position)
        }
    }

    override fun getItemCount(): Int {
        return dataItem!!.size
    }

    inner class BookHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        private var binding: ItemBookmarkBinding? = null
        var img: ImageView?=null
        var videoView: VideoView?=null
        init {
            binding = DataBindingUtil.bind(itemView)
            img=itemView.findViewById(R.id.imageslider101)
            videoView=itemView.findViewById(R.id.player1)
        }
        fun getBinding(): ItemBookmarkBinding {
            return binding!!
        }
    }
}