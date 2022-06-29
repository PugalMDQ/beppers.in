package com.mdq.social.ui.searchdetails

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItems
import com.mdq.social.app.data.response.getshopAlbumDetails.UserItem
import com.mdq.social.databinding.ItemProfileSearchBinding
import com.mdq.social.ui.profile.ProfileActivity

class ProfileSearchAdapter(
    var userItem: List<DataItems>?,
    var context: Context,
) :
    RecyclerView.Adapter<ProfileSearchAdapter.ProHolder>() {

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
        fun onItemClick(locationItem: UserItem)
    }

    override fun onBindViewHolder(holder: ProHolder, position: Int) {
        val binding: ItemProfileSearchBinding = holder.getBinding()

        if(userItem?.get(position)?.profile_photo!=null) {
            Glide.with(context)
                .load("https://mdqualityapps.in/profile/" + userItem?.get(position)?.profile_photo)
                .into(holder.getBinding().imageView41)
        }else{
            holder.getBinding().imageView41.setImageDrawable(context.resources.getDrawable(R.drawable.user))
        }
        holder.getBinding().textView114.setText(userItem?.get(position)?.name)
        holder.getBinding().textView115.setText(userItem?.get(position)?.user_name)
    }

    override fun getItemCount(): Int {
        if(userItem!=null) {
            return userItem!!.size
        }else{
            return 0
        }
    }

    inner class ProHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemProfileSearchBinding? = null
        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }
        fun getBinding(): ItemProfileSearchBinding {
            return binding!!
        }
        override fun onClick(p0: View?) {
            context.startActivity(Intent(context,ProfileActivity::class.java).putExtra("id",userItem?.get(absoluteAdapterPosition)?.id))
        }
    }
}