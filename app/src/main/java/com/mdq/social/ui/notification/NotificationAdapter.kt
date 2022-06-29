package com.mdq.social.ui.notification

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdq.social.R
import com.mdq.social.app.data.response.notification.DataItem
import com.mdq.social.databinding.ItemNotificationBinding
import com.mdq.social.ui.pendingrequest.PendingRequestActivity
import com.mdq.social.ui.post.PostActivity
import com.mdq.social.ui.profile.ProfileActivity
import com.mdq.social.utils.DateUtils

class NotificationAdapter(val context:Context ,val data: List<DataItem?>,var read:readcall) : RecyclerView.Adapter<NotificationAdapter.NotiHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiHolder {
        return NotiHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_notification,
                parent,
                false
            )
        )
    }

    interface readcall{
        fun readcall(id:String)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: NotiHolder, position: Int) {

        if(
            data.get(position)!!.read_notify.equals("1")
        ){
            holder.getBinding().cons.setBackgroundColor(ContextCompat.getColor(context, R.color.status_bar1));
        }else{
            holder.getBinding().cons.setBackgroundColor(ContextCompat.getColor(context, R.color.bg));
        }
//        val i=data.get(position)?.created_at?.toDouble()
        val l: Long? = data.get(position)?.created_at?.toLong()
        holder.getBinding().textView76.setText(data.get(position)?.user_name)
        holder.getBinding().textView79.setText(data.get(position)?.mesg)
//        holder.getBinding().dateTime.setText(data.get(position)?.created_at)
        holder.getBinding().dateTime.setText(DateUtils.getFormattedTime(l!!))

        if(data.get(position)?.profile_photo!=null){
            Glide.with(context).load("http://mdqualityapps.in/profile/"+data.get(position)!!.profile_photo).into(holder.getBinding().imageView52)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class NotiHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener  {
        private var binding: ItemNotificationBinding? = null
        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }
        fun getBinding(): ItemNotificationBinding {
            return binding!!
        }

        @SuppressLint("ResourceAsColor")
        override fun onClick(v: View?) {

            if(data.get(adapterPosition)?.types.equals("followRequest")){

                binding?.cons?.setBackgroundColor(ContextCompat.getColor(context, R.color.bg));

                context.startActivity(Intent(context, PendingRequestActivity::class.java))

                read.readcall(data.get(adapterPosition)?.id!!)

            }else if (data.get(adapterPosition)?.types.equals("followRequestAccepted")){
                binding?.cons?.setBackgroundColor(ContextCompat.getColor(context, R.color.bg));

                context.startActivity(Intent(context,ProfileActivity::class.java).putExtra("id",data.get(adapterPosition)?.from_id.toString().trim()))

                read.readcall(data.get(adapterPosition)?.id!!)

            }else if(data.get(adapterPosition)?.types.equals("Comment")){
                binding?.cons?.setBackgroundColor(ContextCompat.getColor(context, R.color.bg));

                context.startActivity(
                    PostActivity.getCallingIntent(
                        context,
                        data!!.get(bindingAdapterPosition)?.post_id!!.toString().trim(),
                        data!!.get(bindingAdapterPosition)?.id!!.toString().trim(),
                        data!!.get(bindingAdapterPosition)?.from_id!!.toString().trim(),
                        data!!.get(bindingAdapterPosition)?.user_name!!.toString().trim()
                    )
                )
                read.readcall(data.get(adapterPosition)?.id!!)
            }else if(data.get(adapterPosition)?.types.equals("like")){
                binding?.cons?.setBackgroundColor(ContextCompat.getColor(context, R.color.bg));

                context.startActivity(
                    Intent(context,NotificationActivity::class.java)
                        .putExtra("ProfileAdapter", "ProfileAdapter")
                        .putExtra("position",0)
                        .putExtra("user_id",data.get(adapterPosition)?.to_id.toString().trim())
                )

                read.readcall(data.get(adapterPosition)?.id!!)

            }else {
//                Toast.makeText(context, ""+data.get(adapterPosition)?.types, Toast.LENGTH_SHORT).show()
            }
        }
    }
}