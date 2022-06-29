package com.mdq.social.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.databinding.ItemSearchChatBinding

class SearchChatAdapter:RecyclerView.Adapter<SearchChatAdapter.SeaHolder>() {
    private var productsItem: MutableList<String>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeaHolder {
        return SeaHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_chat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 25
    }

    override fun onBindViewHolder(holder: SeaHolder, position: Int) {

    }

    inner class SeaHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemSearchChatBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemSearchChatBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}