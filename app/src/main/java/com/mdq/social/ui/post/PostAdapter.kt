package com.mdq.social.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R
import com.mdq.social.app.data.response.commentdetails.CommentsItem
import com.mdq.social.databinding.ItemCommentsBinding


class PostAdapter(val context: Context,var replyClickManager: ReplyClickManager) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    private var commentsItem: List<CommentsItem>? = null
    private var subPostAdapter: SubPostAdapter? = null

    init {
        commentsItem = ArrayList()
    }

    fun setPostAdapter(itemList: List<CommentsItem>?) {
        if (itemList == null) {
            return
        }
        commentsItem = itemList
        notifyDataSetChanged()
    }

     interface ReplyClickManager {
         fun onItemReplyClick(commentsId: Int,etComment:EditText,post: ConstraintLayout)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comments, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return  2
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var binding: ItemCommentsBinding? = null
        private var recyclerView: RecyclerView? = null

        init {
            binding = DataBindingUtil.bind(itemView)
            binding?.tvPosts?.setOnClickListener(this)
            binding?.textView123?.setOnClickListener(this)

            recyclerView = binding!!.rvSub
            subPostAdapter = SubPostAdapter(context)
            recyclerView!!.adapter = subPostAdapter

        }

        fun getBinding(): ItemCommentsBinding {
            return binding!!
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.textView124 -> {
                    binding?.consAddComments?.visibility=View.VISIBLE
                }
                R.id.textView123 -> {

                }R.id.tv_posts -> {
                replyClickManager.onItemReplyClick(commentsItem?.get(absoluteAdapterPosition)!!.commentsId!!,
                    binding?.stCommt!!,binding?.consAddComments!!
                )
                }
            }
        }
    }
}