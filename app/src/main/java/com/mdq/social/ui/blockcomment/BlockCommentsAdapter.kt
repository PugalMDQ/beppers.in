package com.mdq.social.ui.blockcomment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItems
import com.mdq.social.databinding.ItemBlockCommentsBindingImpl

class BlockCommentsAdapter (val context: Context,var dataItem: List<DataItems>,var blocks:block): RecyclerView.Adapter<BlockCommentsAdapter.BlHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlHolder {
        return BlHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_block_comments,
                parent,
                false
            )
        )
    }
    interface block{
       fun block(userid:String,name:String)
    }

    override fun onBindViewHolder(holder: BlHolder, position: Int) {

        if(dataItem!=null){

            holder.textname?.setText(dataItem.get(position).name)
            holder.textusername?.setText(dataItem.get(position).user_name)

            if(dataItem.get(position).profile_photo!=null){
                Glide.with(context).load("https://mdqualityapps.in/profile/" + dataItem.get(position).profile_photo).into(holder.image!!)
            }else{
                holder.image?.setImageDrawable(context.resources.getDrawable(R.drawable.user))
            }
        }
        holder.BLOCk?.setOnClickListener {
            blocks.block(dataItem.get(position).id!!,dataItem.get(position).user_name!!)
        }

    }

    override fun getItemCount(): Int {
        if(dataItem!=null) {
            return dataItem.size
        }else{
            return 0
        }
    }

    inner class BlHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var binding: ItemBlockCommentsBindingImpl? = null
        var textname:TextView?=null
        var textusername:TextView?=null
        var BLOCk:TextView?=null
        var image:ImageView?=null

        init {
            binding = DataBindingUtil.bind(itemView)
            textname=itemView.findViewById(R.id.textView140)
            textusername=itemView.findViewById(R.id.textView141)
            BLOCk=itemView.findViewById(R.id.BLOCk)
            image=itemView.findViewById(R.id.imageView53)
        }

        fun getBinding(): ItemBlockCommentsBindingImpl {
            return binding!!
        }
    }

}