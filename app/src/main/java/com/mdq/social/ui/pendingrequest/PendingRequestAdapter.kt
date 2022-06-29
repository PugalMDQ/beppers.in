package com.mdq.social.ui.pendingrequest

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.followresponse.FollowerPendingItem
import com.mdq.social.databinding.ItemRequestBinding
import com.mdq.social.ui.follow.FollowerFragment

class PendingRequestAdapter(fm:FragmentManager, val context: Context,val pendingClickManager: PendingClickManager) : FragmentStatePagerAdapter(fm){

    private var dataItem: List<FollowerPendingItem>? = null

    init {
        dataItem = ArrayList()
    }

    fun setPendingRequestAdapter(itemList: List<FollowerPendingItem>) {
        if (itemList == null) {
            return
        }
        dataItem = itemList
        notifyDataSetChanged()
    }

    interface PendingClickManager {
        fun onAcceptClick(followerId: Int)
        fun onDeclineClick(followerId: Int)
    }

    inner class PenHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemRequestBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            binding?.clAccept?.setOnClickListener(this)
            binding?.clDecline?.setOnClickListener(this)
        }

        fun getBinding(): ItemRequestBinding {
            return binding!!
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.cl_accept -> {
                    pendingClickManager.onAcceptClick(dataItem?.get(absoluteAdapterPosition)!!.followerId!!)
                }
                R.id.cl_decline -> {
                    pendingClickManager.onDeclineClick(dataItem?.get(absoluteAdapterPosition)!!.followerId!!)
                }
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment = when (position) {
        0 ->  RequestFragment()
        1 -> IgnoredFragment()
        else -> FollowerFragment()
    }
}