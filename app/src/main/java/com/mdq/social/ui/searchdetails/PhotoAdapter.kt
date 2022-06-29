package com.mdq.social.ui.searchdetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mdq.social.R
import com.mdq.social.app.data.response.getshopAlbumDetails.ImageItem
import com.mdq.social.databinding.ItemPhotoBinding

class PhotoAdapter(var image: List<ImageItem?>?, var context: Context,var isFromAll:Boolean) :
    RecyclerView.Adapter<PhotoAdapter.PhoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoViewHolder {
        return PhoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_photo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhoViewHolder, position: Int) {
        val binding: ItemPhotoBinding = holder.getBinding()

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
        Glide.with(context)
            .load(image?.get(position)?.path)
            .apply(options)
            .into(binding.imageView38)

    }

    override fun getItemCount(): Int {
        return if(isFromAll) if(image?.size!!>=5) 5 else image?.size!! else image?.size!!
    }

    inner class PhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemPhotoBinding? = null
        var imageView: ImageView?=null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
            imageView = itemView.findViewById(R.id.imageView)
        }

        fun getBinding(): ItemPhotoBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}