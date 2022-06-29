package com.mdq.social.ui.searchdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.LocationItem
import com.mdq.social.databinding.ItemLocationBinding

class LocationAdapter(var locationItem:List<LocationItem?>?,var context:Context):RecyclerView.Adapter<LocationAdapter.LocHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocHolder {
        return LocHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_location,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LocHolder, position: Int) {
        val binding: ItemLocationBinding = holder.getBinding()


        binding.responseDataItem=locationItem?.get(position)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.leeminho)
            .error(R.drawable.leeminho)
        Glide.with(context)
            .load(locationItem?.get(position)?.path)
            .apply(options)
            .into(binding.imageView42)
    }

    override fun getItemCount(): Int {
        return locationItem?.size!!
    }

    inner class LocHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemLocationBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemLocationBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}