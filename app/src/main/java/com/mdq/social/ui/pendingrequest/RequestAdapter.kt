package com.mdq.social.ui.pendingrequest

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.connectionrequest.ConnectionRequest
import com.mdq.social.ui.profile.ProfileActivity

class RequestAdapter(var context:Context,var Connection:ConnectionRequest,var clickss:clicks) : RecyclerView.Adapter<RequestAdapter.mine>() {

    interface clicks{
        fun onAcceptClick(position: Int, followerID:String,name:String)
        fun onIgnoreClick(position: Int,  followerID:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestAdapter.mine {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return mine(view)
    }

    override fun onBindViewHolder(holder: RequestAdapter.mine, position: Int) {
        if(Connection.data?.get(position)?.profile_photo!=null){
        Glide.with(context).load("https://mdqualityapps.in/profile/"+Connection.data?.get(position)?.profile_photo).into(holder.imageView51!!)}
        else {
            holder.imageView51!!.setImageDrawable(context.resources.getDrawable(R.drawable.user))
        }
        holder.textView136?.setText(Connection.data?.get(position)?.name)

        holder.cl_accept?.setOnClickListener {
            clickss.onAcceptClick(position, Connection.data?.get(position)?.id!!,Connection.data!!.get(position).user_name!!)
        }
        holder.cl_decline?.setOnClickListener {
            clickss.onIgnoreClick(position, Connection.data?.get(position)?.id!!)
        }
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, ProfileActivity::class.java).putExtra("id",Connection?.data?.get(position)?.id))
        }

    }

    override fun getItemCount(): Int {
        if(Connection.data!=null) {
            return Connection.data!!.size
        }else{
            return 0
        }
    }
    class mine(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        var imageView51:ImageView?=null
        var textView136:TextView?=null
        var cl_accept:TextView?=null
        var cl_decline:TextView?=null
        init {
            imageView51=ItemView.findViewById(R.id.imageView51)
            textView136=ItemView.findViewById(R.id.textView136)
            cl_accept=ItemView.findViewById(R.id.cl_accept)
            cl_decline=ItemView.findViewById(R.id.cl_decline)

        }

    }
}