package com.mdq.social.ui.follow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.followresponse.DataItemses
import com.mdq.social.app.data.response.followresponse.FollowResponse
import com.mdq.social.app.data.response.followresponse.FollowerItem
import com.mdq.social.databinding.ItemFollowerBinding
import com.mdq.social.ui.profile.ProfileActivity

class FollowerAdapter(
    val context: Context,
    var response: FollowResponse,
    var click: clicking,
    var followResponse1: List<DataItemses>?,
    var list1: ArrayList<DataItemses>,
):RecyclerView.Adapter<FollowerAdapter.FoHolder>() {

    private var dataItem: List<FollowerItem>? = null

    interface clicking {
        fun onBlockClick(position: Int, id:String)
    }

    init {
        dataItem = ArrayList()
    }

    fun setFollowerAdapter(itemList: List<FollowerItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoHolder {
        return FoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_follower,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoHolder, position: Int) {
        if(!list1?.get(position)?.active.isNullOrEmpty()) {
            if (list1?.get(position)?.active!!.equals("1")) {
                holder.getBinding().textView76.setText(list1?.get(position)?.name)
                if(!list1?.get(position)?.profile_photo.isNullOrEmpty()) {
                    Glide.with(context)
                        .load("https://mdqualityapps.in/profile/" + list1?.get(position)?.profile_photo)
                        .into(holder.getBinding().imageView52)
                }
            }
            else{

            }
        }else{

        }

        holder.getBinding().clDecline.setOnClickListener {
            click.onBlockClick(position,list1?.get(position)?.id!!)
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProfileActivity::class.java).putExtra("id",list1?.get(position)?.id))
        }

    }

    override fun getItemCount(): Int {
        if(list1!=null) {
            return list1!!.size
        }else{
            return 0
        }
    }

    inner class FoHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        private var binding: ItemFollowerBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun getBinding(): ItemFollowerBinding {
            return binding!!
        }
    }

}