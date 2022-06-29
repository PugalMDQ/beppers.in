package com.mdq.social.ui.signupfreelancer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.databinding.ItemFilesBinding

class FilesAdapter(
    val context: Context,
    var filesList: List<FileItem>,
    var clickmanager: ClickManager
) :
    RecyclerView.Adapter<FilesAdapter.ReviewViewHolder>() {


    interface ClickManager {
        fun onItemDelterClick(position:Int,fileItem: FileItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_files,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {

        holder.getBinding().textView67.text=filesList[position].file.name


        holder.getBinding().imgRemove.setOnClickListener {
            clickmanager.onItemDelterClick(position,filesList.get(position))
        }


    }

    override fun getItemCount(): Int {
        return  filesList.size
    }
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var binding: ItemFilesBinding? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            itemView.setOnClickListener(this)
        }

        fun getBinding(): ItemFilesBinding {
            return binding!!
        }

        override fun onClick(p0: View?) {

        }
    }
}