package com.mdq.social.ui.search

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.recent.DataItem
import com.mdq.social.databinding.ItemTrendingBinding
import com.mdq.social.ui.home.SliderAdapter2

class TrendingAdapter(val context: Context,var clickManager:postClick):RecyclerView.Adapter<TrendingAdapter.TrendHolder>() {
    private var dataItem: List<DataItem>? = null


    interface postClick{
        fun onItemLickClicksOfProfile(id:String,gallery:String,user_id:String,position:Int)
    }

    init {
        dataItem = ArrayList()
    }

    fun setTrendingAdapter(itemList: List<DataItem>?) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendHolder {
        return TrendHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_trending,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataItem!!.size
    }

    override fun onBindViewHolder(holder: TrendHolder, position: Int) {
        val binding: ItemTrendingBinding = holder.getBinding()

        holder.getBinding().muteAndUnmute?.setOnClickListener {
            if (holder.getBinding().muteAndUnmute?.getTag().equals("work")) {
                holder.getBinding().muteAndUnmute!!.setImageResource(R.drawable.ic_pause__1_)
                holder.getBinding().payer1!!.start()
                holder.getBinding().muteAndUnmute.setTag("notwork")
            }else {
                holder.getBinding().muteAndUnmute!!.setImageResource(R.drawable.ic_play__1_)
                holder.getBinding().payer1!!.pause()
                holder.getBinding().muteAndUnmute.setTag("work")
            }
        }
        if (dataItem?.get(position)?.gallery?.contains(".mp4")!!) {
            holder.getBinding().cardformuteAndUnmute!!.visibility=View.VISIBLE
            holder.getBinding().imageSliderSearch!!.visibility=View.GONE
            holder.getBinding().payer1.visibility=View.VISIBLE
            holder.getBinding().payer1.setVideoURI(Uri.parse("https://mdqualityapps.in/gallery/"+ dataItem?.get(position)?.gallery))
            holder.getBinding().payer1.seekTo(1)
        }

        else {
            holder.getBinding().cardformuteAndUnmute!!.visibility=View.GONE
            holder.getBinding().imageSliderSearch!!.visibility=View.VISIBLE
            holder.getBinding().payer1.visibility=View.GONE
            var sliderAdapter: SliderAdapter2?=null
            var uri: ArrayList<Uri>?= ArrayList()
            if(dataItem?.get(position)?.gallery?.contains(",")!!) {
                val str = dataItem?.get(position)?.gallery
                val arr: List<String>? = dataItem?.get(position)?.gallery?.split(",")
                for(i in arr!!.indices)
                {
                    uri?.add(Uri.parse("https://mdqualityapps.in/gallery/"+arr.get(i)))

                }

            }else {
                uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + dataItem?.get(position)?.gallery))
            }

            Glide.with(context)
                .load(uri?.get(0)).into(holder.getBinding().imageSliderSearch)

        }
        holder.itemView.setOnClickListener {
            clickManager.onItemLickClicksOfProfile(
                dataItem!!.get(position).id!!,
                dataItem!!.get(position).gallery!!,
                dataItem!!.get(position).user_id!!,
                position
            )
        }
    }

    inner class TrendHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
       private var binding: ItemTrendingBinding? = null

       init {
           binding = DataBindingUtil.bind(itemView)
           itemView.setOnClickListener(this)
       }

       fun getBinding(): ItemTrendingBinding {
           return binding!!
       }

       override fun onClick(p0: View?) {

       }
    }
}