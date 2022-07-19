package com.mdq.social.ui.reviewlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.ui.business.BusinessProfileNavigator
import com.mdq.social.R
import com.mdq.social.app.data.response.reviewresponse.reviewResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.reviewlist.ReviewListViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityReviewBinding
import com.mdq.social.ui.business.BusinessProfileFragment
import com.mdq.social.ui.business.ReviewAdapter

class ReviewListActivity : BaseActivity<ActivityReviewBinding, ReviewListNavigator>(),
    ReviewListNavigator {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ReviewListActivity::class.java)
        }
    }

    private var activityReviewBinding: ActivityReviewBinding? = null
    private var reviewListViewModel: ReviewListViewModel? = null
    var ii: String? = null
    var adapter: ReviewAdapter? = null
    var i: Int = 0
    var j: Int = 0
    var remarks: Int = 0
    var img1: Int = 0
    var img2: Int = 0
    var img3: Int = 0
    var img4: Int = 0
    var img5: Int = 0
    var businessProfileNavigator: BusinessProfileNavigator? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_review
    }

    override fun getViewBindingVarible(): Int {
        return BR.reviewListViewModel
    }

    override fun getViewModel(): BaseViewModel<ReviewListNavigator> {
        reviewListViewModel =
            ViewModelProvider(this, viewModelFactory).get(ReviewListViewModel::class.java)
        return reviewListViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityReviewBinding = getViewDataBinding()
        activityReviewBinding?.reviewListViewModel = reviewListViewModel
        reviewListViewModel?.navigator = this



        ii = intent?.extras?.getString("mparam")

        if (ii.equals(appPreference.USERID)) {
            activityReviewBinding!!.LinearFeedback.visibility = View.GONE
            activityReviewBinding!!.consAddComment.visibility = View.GONE
        }

        if (ii.isNullOrEmpty()) {
            ii = appPreference.USERID
        }

        if (!appPreference.TYPE.equals("user")) {
            activityReviewBinding!!.LinearFeedback.visibility = View.GONE
            activityReviewBinding!!.consAddComment.visibility = View.GONE
        }

        getReviewList()

        activityReviewBinding!!.tvPost.setOnClickListener {
            if (activityReviewBinding!!.editText.text.length >= 1) {
                if (remarks != 0) {
                    rating()
                } else {
                    Toast.makeText(this, "Give some ratings", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Write some review", Toast.LENGTH_SHORT).show()
            }
        }
        activityReviewBinding?.back?.setOnClickListener {
            onBackPressed()
        }

        activityReviewBinding!!.Img1.setOnClickListener(View.OnClickListener {
            if (img1 == 0) {
                activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
                activityReviewBinding!!.Img2.setClickable(true)
                remarks = remarks + 1
                img1 = 1

                activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.white_star_border))
                activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.white_star_border))
                activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.white_star_border))
                activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.white_star_border))

            } else if (img1 == 1) {
                activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.white_star_border))
                activityReviewBinding!!.Img2.setClickable(true)
                remarks = remarks - 1
                img1 = 0
            }
        })

        activityReviewBinding!!.Img2.setOnClickListener(View.OnClickListener {
//            if (img2 == 0) {
            activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img3.setClickable(true)
            remarks = 2
            img2 = 1
            activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.white_star_border))
            activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.white_star_border))
            activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.white_star_border))

//            } else if (img2 == 1) {
//                activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.white_star_border ))
//                activityReviewBinding!!.Img3.setClickable(true)
//                remarks = remarks - 2
//                img2 = 0
//            }
        })
        activityReviewBinding!!.Img3.setOnClickListener(View.OnClickListener {
//            if (img3 == 0) {
            activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img4.setClickable(true)
            remarks = 3
            img3 = 1

            activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.white_star_border))
            activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.white_star_border))

//            } else if (img3 == 1) {
//                activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img4.setClickable(true)
//                remarks = remarks - 3
//                img3 = 0
//            }
        })
        activityReviewBinding!!.Img4.setOnClickListener(View.OnClickListener {
//            if (img4 == 0) {
            activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img5.setClickable(true)
            remarks = 4
            img4 = 1

            activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.white_star_border))

//            } else if (img4 == 1) {
//                activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img5.setClickable(true)
//                remarks = remarks - 4
//                img4 = 0
//            }
        })
        activityReviewBinding!!.Img5.setOnClickListener(View.OnClickListener {
//            if (img5 == 0) {
            activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.ic_baseline_star_24))
            remarks = 5
            img5 = 1
//            } else if (img5 == 1) {
//                activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.white_star_border))
//                remarks = remarks - 5
//                img5 = 0
//            }
        })
    }

    private fun rating() {
        reviewListViewModel!!.PostRating(
            ii!!,
            remarks!!.toString(),
            activityReviewBinding!!.editText.text.toString()
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val signupResponse = response.data as SignupResponse
                if (signupResponse.message.equals("Rating added successfully!")) {

                    activityReviewBinding!!.Img1.setBackground(resources.getDrawable(R.drawable.white_star_border))
                    activityReviewBinding!!.Img2.setBackground(resources.getDrawable(R.drawable.white_star_border))
                    activityReviewBinding!!.Img3.setBackground(resources.getDrawable(R.drawable.white_star_border))
                    activityReviewBinding!!.Img4.setBackground(resources.getDrawable(R.drawable.white_star_border))
                    activityReviewBinding!!.Img5.setBackground(resources.getDrawable(R.drawable.white_star_border))
                    remarks = 0
                    img5 = 0
                    getReviewList()

                    var businessProfileFragment:BusinessProfileFragment= BusinessProfileFragment();
                    businessProfileFragment.revieadded()
                    activityReviewBinding!!.editText.setText("")

                } else {
                    showToast(signupResponse.message!!)
                }
            }
        })

    }


    private fun getReviewList() {
        reviewListViewModel!!.getReviewList(ii!!).observe(this, Observer { response ->
            if (response?.data != null) {
                val reviewResponses = response.data as reviewResponse
                if (reviewResponses.data != null) {
                    activityReviewBinding!!.textView50.visibility = View.GONE
                    activityReviewBinding!!.imageView58.visibility = View.GONE
                    adapter = ReviewAdapter(this, reviewResponses.data!!)
                    activityReviewBinding!!.rv.adapter = adapter
                } else {
                    activityReviewBinding!!.textView50.visibility = View.VISIBLE
                    activityReviewBinding!!.imageView58.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onClick() {
        onBackPressed()
    }
}