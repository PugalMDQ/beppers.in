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

class IgnoredAdapter(var context: Context, var Connection: ConnectionRequest) : RecyclerView.Adapter<IgnoredAdapter.mine>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IgnoredAdapter.mine {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ignored, parent, false)

        return mine(view)
    }

    override fun onBindViewHolder(holder: IgnoredAdapter.mine, position: Int) {

        if(Connection.data?.get(position)?.profile_photo!=null){
            Glide.with(context).load("https://mdqualityapps.in/profile/"+Connection.data?.get(position)?.profile_photo).into(holder.imageView52!!)}
        else {
            holder.imageView52!!.setImageDrawable(context.resources.getDrawable(R.drawable.user))
        }
        holder.textView76?.setText(Connection.data?.get(position)?.name)
        holder.textView79?.setText(Connection.data?.get(position)?.user_name)

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

        var imageView52:ImageView?=null
        var textView76:TextView?=null
        var textView79:TextView?=null

        init {
            imageView52=ItemView.findViewById(R.id.imageView52)
            textView76=ItemView.findViewById(R.id.textView76)
            textView79=ItemView.findViewById(R.id.textView79)
        }
    }

}