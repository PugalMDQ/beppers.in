package com.mdq.social.ui.Comments

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.siyamed.shapeimageview.CircularImageView
import com.mdq.social.R
import com.mdq.social.app.data.response.CommentResponse.CommentResponse
import com.mdq.social.ui.profile.ProfileActivity
import com.mdq.social.utils.DateUtils

class AdapterForComment(val context: Context,var response: CommentResponse):
    RecyclerView.Adapter<AdapterForComment.mine>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mine {

        var view:View=
            LayoutInflater.from(parent.context).inflate(R.layout.item_for_comments,parent,false)
        return mine(view)

    }

    override fun onBindViewHolder(holder: mine, position: Int) {
        val l: Long? = response.data?.get(position)?.created_at?.toLong()
        holder.ProfileComments?.setText(response.data?.get(position)?.comments)
        holder.ProfileName?.setText(response.data?.get(position)?.user_name)
        holder.count?.setText(DateUtils.getFormattedTime(l!!))

        if(response.data!!.get(position)?.profile_photo!=null) {
            Glide.with(context)
                .load("https://mdqualityapps.in/profile/" + response.data!!.get(position)?.profile_photo)
                .into(holder.img!!)
        }
    }

    override fun getItemCount(): Int {
       return response.data!!.size
    }

    inner class mine(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var img: CircularImageView?=null
        var ProfileName: TextView?=null
        var ProfileComments: TextView?=null
        var count: TextView?=null

        init {
            img=itemView.findViewById(R.id.ProfileImage)
            ProfileName=itemView.findViewById(R.id.ProfileName)
            ProfileComments=itemView.findViewById(R.id.ProfileComments)
            count=itemView.findViewById(R.id.count)

            itemView.setOnClickListener {
                context.startActivity(Intent(context, ProfileActivity::class.java).putExtra("id",response?.data?.get(absoluteAdapterPosition)?.user_id))
            }

        }
    }
}