package com.mdq.social.ui.livechat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.livechat.DataItem
import com.mdq.social.databinding.ItemChatBinding
import com.mdq.social.ui.models.ChatMessage
import java.util.HashMap

class LiveChatAdapter(
    val context: Context,
    var data: ArrayList<ChatMessage>,
    var fromId: String,
    var to_id: String,
    var latestMessagesMap: HashMap<String, ChatMessage>,

    ) : RecyclerView.Adapter<LiveChatAdapter.LiveHolder>() {

    private var dataItem: List<DataItem>? = null

    init {
        dataItem = ArrayList()
    }

    fun setLiveChatAdapter(itemList: List<DataItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveHolder {
        return LiveHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LiveHolder, position: Int) {
        Log.i("fromId",fromId)
        Log.i("toId",to_id)

        Log.i("FFromId", data[position].fromId.toString())
        Log.i("FTOID", data[position].toId.toString())
        if(fromId.trim().equals(data[position].fromId.toString().trim())){
            holder.getBinding().textView68.visibility=View.VISIBLE
            holder.getBinding().textView72.visibility=View.GONE
            holder.getBinding().textView68.setText(data[position].text.toString())
        }else if (to_id.toString().trim().equals(data[position].toId.toString().trim())){
            holder.getBinding().textView68.visibility=View.GONE
            holder.getBinding().textView72.visibility=View.VISIBLE
            holder.getBinding().textView72.setText(data[position].text.toString())
        }

    }

    override fun getItemCount(): Int {
        return latestMessagesMap.size
    }

    inner class LiveHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ItemChatBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
        }

        fun getBinding(): ItemChatBinding {
            return binding!!
        }
    }

}