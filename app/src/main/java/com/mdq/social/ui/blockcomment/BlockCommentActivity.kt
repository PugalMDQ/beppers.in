package com.mdq.social.ui.blockcomment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.commentblocklist.DataItem
import com.mdq.social.app.data.response.commentblocklist.commentBlockList
import com.mdq.social.app.data.response.getshopAlbumDetails.UserSearchDetailResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.blockcomment.BlockCommentViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityBlockCommentBinding
import kotlinx.android.synthetic.main.activity_block.*

class BlockCommentActivity: BaseActivity<ActivityBlockCommentBinding, BlockCommentNavigator>(),
    BlockCommentNavigator, TextWatcher,BlockCommentsAdapter.block ,BlockedCommentListAdapter.blockes{
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, BlockCommentActivity::class.java)
        }
    }

    private var activityBlockCommentBinding: ActivityBlockCommentBinding? = null
    private var blockCommentViewModel: BlockCommentViewModel? = null
    private var blockCommentsAdapter:BlockCommentsAdapter?=null
    private var blockedCommentListAdapter:BlockedCommentListAdapter?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_block_comment
    }

    override fun getViewModel(): BaseViewModel<BlockCommentNavigator> {
        blockCommentViewModel =
            ViewModelProvider(this, viewModelFactory).get(BlockCommentViewModel::class.java)
        return blockCommentViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.blockCommentViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBlockCommentBinding = getViewDataBinding()
        activityBlockCommentBinding?.blockCommentViewModel = blockCommentViewModel
        blockCommentViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        getBlockedList()
        activityBlockCommentBinding?.editTextTextPersonName15?.addTextChangedListener(this)

        activityBlockCommentBinding!!.imageView.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getProfile() {
        blockCommentViewModel!!.getSearchUserDetails(
            activityBlockCommentBinding?.editTextTextPersonName15?.text.toString()
        )
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val getShopAlbumDetailsResponse =
                        response.data as UserSearchDetailResponse
                    if (getShopAlbumDetailsResponse != null && getShopAlbumDetailsResponse?.data != null) {
                        blockCommentsAdapter=BlockCommentsAdapter(this,getShopAlbumDetailsResponse.data,this)
                        rv_block.adapter = blockCommentsAdapter
                    } else {

                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }
    private fun getBlockedList() {

        blockCommentViewModel!!.getCommentBlockList(appPreference.USERID).observe(this, Observer { response ->
            if (response?.data != null) {
                val commentBlockLists = response.data as commentBlockList
                if (commentBlockLists != null) {
                    blockedCommentListAdapter=BlockedCommentListAdapter(this,
                        commentBlockLists.data as List<DataItem>,this)
                        rv_block.adapter = blockedCommentListAdapter
                } else {
                    blockedCommentListAdapter=BlockedCommentListAdapter(this,
                        commentBlockLists.data as List<DataItem>,this)
                    rv_block.adapter = blockedCommentListAdapter
                    showToast("No user blocked")
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onClick(status: Int) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        getProfile()
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun block(userid: String,name:String) {
        blockCommentViewModel!!.CommentBlock(
            userid,
        )
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val signupResponse =
                        response.data as SignupResponse
                    if(signupResponse.message.equals("Comment has been blocked!")){
                        Toast.makeText(this, "Blocked  "+name, Toast.LENGTH_SHORT).show()
                        getBlockedList()
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    override fun unblocks(userid: String, name: String) {

        blockCommentViewModel!!.CommentUNBlock(
            userid,
        )
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val signupResponse =
                        response.data as SignupResponse
                    if(signupResponse.message.equals("Comment has been unblocked!")){
                        Toast.makeText(this, "UnbBlocked  "+name, Toast.LENGTH_SHORT).show()
                        getBlockedList()
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }
}