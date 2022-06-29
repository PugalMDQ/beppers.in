package com.mdq.social.ui.searchdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.VediosItem
import com.mdq.social.databinding.ItemVideoBinding

class AllVideoAdapter(var videoItem:List<VediosItem?>?, var context: Context):RecyclerView.Adapter<AllVideoAdapter.AllvHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllvHolder {
        return AllvHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_video,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return  if(videoItem?.size!!>=5) 5 else videoItem?.size!!

    }

    override fun onBindViewHolder(holder: AllvHolder, position: Int) {
        val binding: ItemVideoBinding = holder.getBinding()
        setImage(binding, videoItem,position)
        binding.responseDataItem=videoItem?.get(position)
    }

    private fun setImage(binding: ItemVideoBinding, videoItem: List<VediosItem?>?, position: Int) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_no_pictures)
            .error(R.drawable.ic_no_pictures)
            .transform(RoundedCorners(100))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context)
            .load(videoItem?.get(position)?.thumbnail)
            .apply(options)
            .into(binding.imageView31)
    }

    inner class AllvHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemVideoBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemVideoBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }


    }


}