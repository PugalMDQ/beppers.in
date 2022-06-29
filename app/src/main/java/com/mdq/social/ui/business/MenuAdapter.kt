package com.mdq.social.ui.business

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.RateCard.DataItem

class MenuAdapter(val context:Context,var items:ArrayList<String>,var click:clicks,var data: List<DataItem>) :
    RecyclerView.Adapter<MenuAdapter.Menuclass>() {

    interface clicks{
        fun click(id:String,url:String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuAdapter.Menuclass{
        var view:View=LayoutInflater.from(parent.context).inflate(R.layout.item_menu_list,parent,false)
        return Menuclass(view)
    }

    override fun onBindViewHolder(holder: MenuAdapter.Menuclass, position: Int) {
            Glide.with(context)
                .load(Uri.parse("https://mdqualityapps.in/ratecard/"+items[position]))
                .into(holder.img!!)

        holder.itemView.setOnClickListener {
            click.click(data.get(position).id!!,"https://mdqualityapps.in/ratecard/"+data.get(position).ratecard!!)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class Menuclass(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        var img:ImageView?=null

        init {
            img=itemView.findViewById(R.id.menuOfShop)

        }

    }

}