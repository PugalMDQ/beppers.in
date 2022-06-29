package com.mdq.social.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mdq.social.R
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.databinding.ItemExploreBinding

class ExploreAdapter(var context: Context) : RecyclerView.Adapter<ExploreAdapter.ExpHolder>() {

    private var dataItem: List<DataItem>? = null

    init {
        dataItem = ArrayList()
    }

    fun setExploreAdapter(itemList: List<DataItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpHolder {
        return ExpHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_explore,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataItem!!.size
    }

    override fun onBindViewHolder(holder: ExpHolder, position: Int) {
        val item: DataItem = dataItem!!.get(position)
        val binding: ItemExploreBinding = holder.getBinding()
        setImage(binding, item)
        binding.dataItem = item
    }

    private fun setImage(binding: ItemExploreBinding, item: DataItem) {
        val options: RequestOptions = RequestOptions()
            .circleCrop()
            .placeholder(R.drawable.ic_no_pictures)
            .error(R.drawable.ic_no_pictures)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    }

    inner class ExpHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemExploreBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemExploreBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}