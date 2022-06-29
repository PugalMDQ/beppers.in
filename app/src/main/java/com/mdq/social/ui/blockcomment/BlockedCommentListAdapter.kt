package com.mdq.social.ui.blockcomment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.commentblocklist.DataItem

class BlockedCommentListAdapter(val context: Context, var dataItem: List<DataItem>, var blocks:blockes): RecyclerView.Adapter<BlockedCommentListAdapter.BlHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlHolder {
        return BlHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_block,
                parent,
                false
            )
        )
    }
    interface blockes{
        fun unblocks(userid:String,name:String)
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
            blocks.unblocks(dataItem.get(position).block_user_id!!,dataItem.get(position).user_name!!)
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
        var textname: TextView?=null
        var textusername: TextView?=null
        var BLOCk: TextView?=null
        var image: ImageView?=null

        init {
            textname=itemView.findViewById(R.id.textView141)
            textusername=itemView.findViewById(R.id.textView140)
            BLOCk=itemView.findViewById(R.id.unBlock)
            image=itemView.findViewById(R.id.imageView53)
        }

    }

}