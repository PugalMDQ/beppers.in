package com.vkart.phoneauth.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import java.util.*

class SelectedImageAdapter(var context: Context, var uri: ArrayList<Uri>) : RecyclerView.Adapter<SelectedImageAdapter.Mine>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageAdapter.Mine {
        return Mine(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_for_selectedimage,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return uri.size
    }

    override fun onBindViewHolder(holder: SelectedImageAdapter.Mine, position: Int) {
        holder.img!!.setImageURI(uri.get(position))
    }

    inner class Mine(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var img:ImageView?=null
        init {
            img=itemView.findViewById(R.id.selectedImage);
        }
    }

    }
