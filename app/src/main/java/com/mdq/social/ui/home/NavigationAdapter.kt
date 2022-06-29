package com.mdq.social.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.navigationmenu.NevigationMenuItem
import com.mdq.social.databinding.LayoutHeaderBinding

class NavigationAdapter(val context: Context, val navigationClickManager:NavigationClickManager):RecyclerView.Adapter<NavigationAdapter.NavHolder>() {

    private var nevigationMenuItem: List<NevigationMenuItem>? = null
    private var navigationClickManagers: NavigationClickManager? = null

    init {
        nevigationMenuItem = ArrayList()
        this.navigationClickManagers=navigationClickManager
    }

    fun setNavigationAdapter(itemList: List<NevigationMenuItem>?) {
        if (itemList == null) {
            return
        }
        nevigationMenuItem = itemList
        notifyDataSetChanged()
    }

    interface  NavigationClickManager{
        fun onClick(topicsItem: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavHolder {
        return NavHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_header,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NavHolder, position: Int) {
        val item: NevigationMenuItem = nevigationMenuItem!!.get(position)
        val binding: LayoutHeaderBinding = holder.getBinding()
        binding.nevigationMenuItem = item
    }

    override fun getItemCount(): Int {
        return nevigationMenuItem?.size!!
    }

    inner class NavHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: LayoutHeaderBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): LayoutHeaderBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {
            navigationClickManagers!!.onClick(adapterPosition)
        }

    }
}