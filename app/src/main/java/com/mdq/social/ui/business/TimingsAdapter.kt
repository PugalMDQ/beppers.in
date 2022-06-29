package com.mdq.social.ui.business

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.ShopItem
import com.mdq.social.databinding.ItemTimingsBinding

class TimingsAdapter(val context: Context, val response: ShopItem,val  days: ArrayList<String>):RecyclerView.Adapter<TimingsAdapter.TimingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimingViewHolder {
        return TimingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_timings,
                parent,
                false
            )
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TimingViewHolder, position: Int) {
        if(response.data!=null) {
            if (response.data.get(position)!=null) {
                if (!response.data.get(position)!!.to_time.isNullOrEmpty() && !response.data.get(position)!!.from_time.isNullOrEmpty()) {
                    holder.getBinding().tvDay.setText(days.get(position))
                    holder.getBinding().tvTiming.setText(
                        response.data?.get(position)?.from_time + " - " + response.data?.get(
                            position
                        )?.to_time
                    )
                } else {
                    holder.getBinding().tvDay.setBackgroundResource(R.drawable.black_day)
                    holder.getBinding().tvDay.setText(days.get(position))
                    holder.getBinding().tvTiming.setText("closed")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return response.data!!.size
    }
    inner class TimingViewHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemTimingsBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemTimingsBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}