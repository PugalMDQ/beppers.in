package com.mdq.social.ui.Comments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.Comments.CommentsViewModel
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityCommentsBinding
import com.mdq.social.ui.post.PostNavigator

class Comments : BaseActivity<ActivityCommentsBinding, PostNavigator>(), PostNavigator {

    private var commentsViewModel: CommentsViewModel? = null
    private var PostId: String? = null
    private var activityPostBinding: ActivityCommentsBinding? = null

    override fun getViewModel(): BaseViewModel<PostNavigator> {
        commentsViewModel =
            ViewModelProvider(this, viewModelFactory).get(CommentsViewModel::class.java)
        return commentsViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        activityPostBinding = getViewDataBinding()

        if (intent.extras != null) {
            PostId = intent!!.extras!!.getString("PostID").toString()
        }

        getCommentDetails(PostId!!)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_comments
    }

    override fun getViewBindingVarible(): Int {
        TODO("Not yet implemented")
    }

    private fun getCommentDetails(albumId: String) {
    }

}