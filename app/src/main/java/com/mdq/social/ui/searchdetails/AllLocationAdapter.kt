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
import com.mdq.social.databinding.ItemAllLocationBinding

class AllLocationAdapter(var locationItem:List<LocationItem?>?, var context: Context):RecyclerView.Adapter<AllLocationAdapter.AllLHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllLHolder {
        return AllLHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_all_location,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return  if(locationItem?.size!!>=5) 5 else locationItem?.size!!

    }

    override fun onBindViewHolder(holder: AllLHolder, position: Int) {
        val binding: ItemAllLocationBinding = holder.getBinding()

        binding.responseDataItem=locationItem?.get(position)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.leeminho)
            .error(R.drawable.leeminho)
        Glide.with(context)
            .load(locationItem?.get(position)?.path)
            .apply(options)
            .into(binding.imageView42)
    }

   inner class AllLHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
       private var binding: ItemAllLocationBinding? = null

       init {
           binding = DataBindingUtil.bind(itemView)
           itemView.setOnClickListener(this)
       }

       fun getBinding(): ItemAllLocationBinding {
           return binding!!
       }

       override fun onClick(p0: View?) {

       }
    }
}