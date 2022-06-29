package com.mdq.social.ui.blockaccount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.blocklist.DataItem
import com.mdq.social.app.data.response.connectionrequest.dateItems
import com.mdq.social.databinding.ItemBlockBinding

class BlockAccountAdapter(val context: Context, var response: List<dateItems>?, var unBlockClicks: unBlockClick):RecyclerView.Adapter<BlockAccountAdapter.BlHolder>() {
    private var dataItem: List<DataItem>? = null

    interface unBlockClick{
        fun unBlockClick(user_id:String)
    }

    init {
        dataItem = ArrayList()
    }

    fun setBlockAccountAdapter(itemList: List<DataItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlHolder {
        return BlHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_block,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BlHolder, position: Int) {

        if(response?.get(position)?.profile_photo!=null){
            Glide.with(context).load("https://mdqualityapps.in/profile/"+response?.get(position)?.profile_photo).into(holder.getBinding().imageView53!!)}

        else {
            holder.getBinding().imageView53!!.setImageDrawable(context.resources.getDrawable(R.drawable.user))
        }

        holder.getBinding().textView140?.setText(response?.get(position)?.name)
        holder.getBinding().textView141?.setText(response?.get(position)?.user_name)
        holder.getBinding().unBlock.setOnClickListener {
            unBlockClicks.unBlockClick(response?.get(position)?.id!!)
        }
    }

    override fun getItemCount(): Int {
        if(response!=null) {
            return response?.size!!
        }else{
            return 0
        }
    }

    inner class BlHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        private var binding: ItemBlockBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }
        fun getBinding(): ItemBlockBinding {
            return binding!!
        }
    }
}