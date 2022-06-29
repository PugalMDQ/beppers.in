package com.mdq.social.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.bookmarklist.BookMarkListResponse
import com.mdq.social.app.data.response.bookmarklist.BookmarkListItem
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.bookmark.BookMarkViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.FragmentBookmarkBinding
import com.mdq.social.ui.notification.NotificationActivity
import kotlinx.android.synthetic.main.fragment_bookmark.*

class BookMarkActivity : BaseActivity<FragmentBookmarkBinding, BookMarkNavigator>(),
    BookMarkNavigator,BookMarkAdapter.ClickManager {
    private var bookMarkViewModel: BookMarkViewModel? = null
    private var fragmentBookmarkBinding: FragmentBookmarkBinding? = null
    private var bookMarkAdapter:BookMarkAdapter?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_bookmark
    }

    override fun getViewModel(): BaseViewModel<BookMarkNavigator> {
        bookMarkViewModel =
            ViewModelProvider(this, viewModelFactory).get(BookMarkViewModel::class.java)
        return bookMarkViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.bookMarkViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBookmarkBinding = getViewDataBinding()
        fragmentBookmarkBinding?.bookMarkViewModel = bookMarkViewModel
        bookMarkViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        fragmentBookmarkBinding?.imgBack?.setOnClickListener {
            onBackPressed()
        }
        getBookmark()

    }

    override fun onItemLickClicksOfProfile(id: String, gallery: String, user_id: String,position:Int) {
        startActivity(
            Intent(applicationContext, NotificationActivity::class.java)
                .putExtra("BookmarkAdapter", "BookmarkAdapter")
                .putExtra("position",position)
                .putExtra("user_id",user_id)
        )
    }

    private fun getBookmark() {
        bookMarkViewModel!!.getBookmark()
            .observe(this, Observer { response ->
                hideLoading()
                if (response?.data != null) {
                    val bookMarkListResponse = response.data as BookMarkListResponse
                    if (bookMarkListResponse != null && bookMarkListResponse?.data != null) {
                        if (bookMarkListResponse.data.size != 0) {
                            hideLoading()
                            bookMarkAdapter = BookMarkAdapter(applicationContext,bookMarkListResponse.data!!,this)
                            fragmentBookmarkBinding?.bookMarkAdapter = bookMarkAdapter
                            fragmentBookmarkBinding?.rvBook?.adapter=bookMarkAdapter
                        } else {
                        }
                    } else {
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }
}