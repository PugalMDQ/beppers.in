package com.mdq.social.ui.addpost

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItems
import com.mdq.social.databinding.ItemProfileSearchBinding

class profileSearchAdapterForAddpost ( var userItem: List<DataItems>?,
var context: Context,
 var clicks:ClickManager
) :
RecyclerView.Adapter<profileSearchAdapterForAddpost.ProHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProHolder {
        return ProHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_profile_search,
                parent,
                false
            )
        )
    }

    interface ClickManager {
        fun onItemClick(user_id:String,name:String)
    }

    override fun onBindViewHolder(holder: ProHolder, position: Int) {
        if(userItem?.get(position)?.profile_photo!=null) {
            Glide.with(context)
                .load("https://mdqualityapps.in/profile/" + userItem?.get(position)?.profile_photo)
                .into(holder.getBinding().imageView41)
        }else{
            holder.getBinding().imageView41.setImageDrawable(context.resources.getDrawable(R.drawable.user))
        }
        holder.getBinding().textView114.setText(userItem?.get(position)?.name)
        holder.getBinding().textView115.setText(userItem?.get(position)?.description)
        holder.itemView.setOnClickListener {
            clicks.onItemClick(userItem?.get(position)?.id!!,userItem?.get(position)?.user_name!!)
        }
    }

    override fun getItemCount(): Int {
        if(userItem!=null) {
            return userItem!!.size
        }else{
            return 0
        }
    }

    inner class ProHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var binding: ItemProfileSearchBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }
        fun getBinding(): ItemProfileSearchBinding {
            return binding!!
        }
    }
}