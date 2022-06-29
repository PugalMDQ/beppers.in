package com.mdq.social.ui.follow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.followresponse.FollowResponse
import com.mdq.social.app.data.response.followresponse.FollowingItem
import com.mdq.social.databinding.ItemFollowingBinding
import com.mdq.social.ui.profile.ProfileActivity

class FollowingAdapter(val context: Context, var response: FollowResponse,var click:clickes):RecyclerView.Adapter<FollowingAdapter.FollHolder>() {
    private var dataItem: List<FollowingItem>? = null

    interface clickes {
        fun onBlocksClick(position: Int, id:String)
        fun onunfollowClick(position: Int, id:String)
    }

    init {
        dataItem = ArrayList()
    }

    fun setFollowingAdapter(itemList: List<FollowingItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollHolder {
        return FollHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_following,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollHolder, position: Int) {
        if(response.data!=null) {
            if(!response.data?.get(position)?.profile_photo.isNullOrEmpty()) {
                Glide.with(context)
                    .load("https://mdqualityapps.in/profile/" + response.data?.get(position)?.profile_photo)
                    .into(holder.getBinding().imageView52)
            }
            holder.getBinding().textView76.setText(response.data?.get(position)?.name)
        }
        holder.unfollow?.setOnClickListener {
            click.onunfollowClick(position,response.data?.get(position)?.id!!)
        }
        holder.block?.setOnClickListener {
            click.onBlocksClick(position,response.data?.get(position)?.id!!)
        }
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProfileActivity::class.java).putExtra("id",response?.data?.get(position)?.id))
        }
    }

    override fun getItemCount(): Int {
        if(response.data!=null) {
            return response.data!!.size
        }else{
            return 0
        }
    }

    inner class FollHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        private var binding: ItemFollowingBinding? = null

        var unfollow:TextView?=null
        var block:TextView?=null
        init {
            binding = DataBindingUtil.bind(itemView)
            unfollow=itemView.findViewById(R.id.cl_decline)
            block=itemView.findViewById(R.id.cl_Block)
        }
        fun getBinding(): ItemFollowingBinding {
            return binding!!
        }

    }

}