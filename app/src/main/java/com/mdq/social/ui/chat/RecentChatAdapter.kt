package com.mdq.social.ui.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.livechat.count
import com.mdq.social.app.data.response.livechatprofile.DataItem
import com.mdq.social.databinding.ItemChatRecentBinding
import com.mdq.social.ui.livechat.LiveChatActivity
import com.mdq.social.utils.DateUtils

class RecentChatAdapter(
    val context: Context,
    var fromID: String,
    var data: List<DataItem?>,
    var username: String,
    var FFRomID: String,
    var clk: click,
    var data1: List<count>?
) :
    RecyclerView.Adapter<RecentChatAdapter.RecHolder>() {

    interface click{
        fun click(textview: TextView,text:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {

        return RecHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat_recent,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {

        if(data1!=null) {
            for (i in data1!!.indices) {
                if (data.get(position)?.from_id?.trim().equals(data1?.get(i)?.from_id)) {
                    holder.getBinding().textView121.visibility=View.VISIBLE
                    holder.getBinding().textView121.setText(data1?.get(i)?.Count)
                } else if (data.get(position)?.to_id?.trim().equals(data1?.get(i)?.from_id)) {
                    holder.getBinding().textView121.visibility=View.VISIBLE
                    holder.getBinding().textView121.setText(data1?.get(i)?.Count)
                }else{
                    holder.getBinding().textView121.visibility=View.GONE
                }
            }
        }else{
            holder.getBinding().textView121.visibility=View.GONE
        }

        if(!username.equals(data.get(position)?.from_username?.trim())) {
            holder.getBinding().textView119.setText(data.get(position)?.latest_message)
            val l: Long? = data.get(position)?.created_time?.toLong()
            holder.getBinding().textView120.setText(DateUtils.getFormattedTime(l!!))
            if (!data.get(position)?.from_profile.isNullOrEmpty()) {
                Glide.with(context)
                    .load("http://mdqualityapps.in/profile/" + data.get(position)?.from_profile)
                    .into(holder.getBinding().imageView43)
            }
            holder.getBinding().textView118.setText(data.get(position)?.from_username)
        }else{
            holder.getBinding().textView119.setText(data.get(position)?.latest_message)
            val l: Long? = data.get(position)?.created_time?.toLong()
            holder.getBinding().textView120.setText(DateUtils.getFormattedTime(l!!))
            if (!data.get(position)?.to_profile.isNullOrEmpty()) {
                Glide.with(context)
                    .load("http://mdqualityapps.in/profile/" + data.get(position)?.to_profile)
                    .into(holder.getBinding().imageView43)
            }
            holder.getBinding().textView118.setText(data.get(position)?.to_username)

        }
    }

    inner class RecHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
         var binding: ItemChatRecentBinding? = null
        var text:TextView?=null
        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        @JvmName("getBinding1")
        fun getBinding(): ItemChatRecentBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {
            clk.click(getBinding().textView119,getBinding().textView119.text.toString())
            if(fromID.trim().equals(data.get(absoluteAdapterPosition)?.from_id?.trim())) {
                var intent: Intent = Intent(context, LiveChatActivity::class.java)
                intent.putExtra("to_id", data.get(absoluteAdapterPosition)?.to_id?.trim())
                intent.putExtra("name", data.get(absoluteAdapterPosition)?.to_username)
                intent.putExtra(
                    "image",
                    "http://mdqualityapps.in/profile/" + data.get(absoluteAdapterPosition)?.to_profile
                )
                if(FFRomID.trim().equals(data.get(absoluteAdapterPosition)?.from_firebaseuser?.trim())){
                    intent.putExtra("ToFireBaseID", data.get(absoluteAdapterPosition)?.to_firebaseuser?.trim())
                }else{
                    intent.putExtra("ToFireBaseID", data.get(absoluteAdapterPosition)?.from_firebaseuser?.trim())

                }
                intent.putExtra("Blockedid", data.get(absoluteAdapterPosition)?.chat_block?.trim())

                context.startActivity(intent)
            }else{
                var intent: Intent = Intent(context, LiveChatActivity::class.java)
                intent.putExtra("to_id", data.get(absoluteAdapterPosition)?.from_id?.trim())
                intent.putExtra("name", data.get(absoluteAdapterPosition)?.from_username)
                intent.putExtra(
                    "image",
                    "http://mdqualityapps.in/profile/" + data.get(absoluteAdapterPosition)?.from_profile
                )
                if(FFRomID.trim().equals(data.get(absoluteAdapterPosition)?.from_firebaseuser?.trim())){
                    intent.putExtra("ToFireBaseID", data.get(absoluteAdapterPosition)?.to_firebaseuser?.trim())
                }else{
                    intent.putExtra("ToFireBaseID", data.get(absoluteAdapterPosition)?.from_firebaseuser?.trim())
                }
                intent.putExtra("Blockedid", data.get(absoluteAdapterPosition)?.chat_block?.trim())

                context.startActivity(intent)
            }
        }
    }
}