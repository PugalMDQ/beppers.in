package com.mdq.social.ui.signupbusiness

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.location.DataItem
import com.mdq.social.databinding.ItemCategoryBinding

class LocationAdapter(var context: Context, var dataItemList: List<DataItem>, var locationManager:  LocationManager):RecyclerView.Adapter<LocationAdapter.LocHolder>() {

    interface LocationManager{
        fun onItemClick(dataItemList: DataItem, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocHolder {
        return LocHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LocHolder, position: Int) {
        holder.getBinding().tvCategory.setText(dataItemList.get(position).location)
    }

    override fun getItemCount(): Int {
        return dataItemList.size
    }

    inner class LocHolder(itemView:View):RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemCategoryBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemCategoryBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {
            locationManager.onItemClick(dataItemList.get(absoluteAdapterPosition),absoluteAdapterPosition)
        }
    }
}