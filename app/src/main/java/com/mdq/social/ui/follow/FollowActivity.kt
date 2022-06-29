package com.mdq.social.ui.follow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amilfreight.amilfreight.data.models.tab.TabsModel
import com.google.android.material.tabs.TabLayout
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.followresponse.DataItemses
import com.mdq.social.app.data.response.followresponse.FollowResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.follow.FollowViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityFollowBinding
import com.mdq.social.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_follow.*

class FollowActivity : BaseActivity<ActivityFollowBinding, FollowNavigator>(),FollowNavigator ,FollowerAdapter.clicking,FollowingAdapter.clickes{

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, FollowActivity::class.java)
        }
    }
    private var activityFollowBinding: ActivityFollowBinding? = null
    private var followViewModel: FollowViewModel? = null
    private var mAdapter: TabAdapter? = null
    val tabsList = ArrayList<TabsModel>()
    var followFragment=FollowerFragment.getInstance()
    var followingFragment=FollowingFragment.getInstance()
    var tab_position:Int?=null
    var numbers:Int?=0
    var userResponse:FollowResponse?=null
    var followAdapter:FollowerAdapter?=null
    var followingAdapter:FollowingAdapter?=null


    override fun getLayoutId(): Int {
        return R.layout.activity_follow
    }

    override fun getViewModel(): BaseViewModel<FollowNavigator> {
        followViewModel =
            ViewModelProvider(this, viewModelFactory).get(FollowViewModel::class.java)
        return followViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.followViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFollowBinding = getViewDataBinding()
        activityFollowBinding?.followViewModel = followViewModel
        followViewModel?.navigator = this
        getFollowFollowingList(appPreference.USERID)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        tab_position = tb_trip.selectedTabPosition

        tb_trip.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab_position = tab.position
                getFollowFollowingList(appPreference.USERID)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun getFollowFollowingList(userId: String) {
        if (tab_position==1){
            activityFollowBinding?.rvFollow?.visibility= View.GONE
            activityFollowBinding?.rvFollowing?.visibility= View.VISIBLE
            followViewModel!!.getFollowingList(
                appPreference.USERID
                ).observe(this, Observer { response ->

                    val followResponse1 = response.data as FollowResponse

                    if (followResponse1.data!=null) {
                        followingAdapter = FollowingAdapter(this, followResponse1,this)
                        activityFollowBinding?.rvFollowing?.adapter = followingAdapter
                    } else {
                        followingAdapter = FollowingAdapter(this, followResponse1,this)
                        activityFollowBinding?.rvFollowing?.adapter = followingAdapter
                    }
            })
        }
        else {
            activityFollowBinding?.rvFollow?.visibility= View.VISIBLE
            activityFollowBinding?.rvFollowing?.visibility= View.GONE
            followViewModel!!.getfollowFollowingList(
                appPreference.USERID,
                ).observe(this, Observer { response ->
                if (response?.data != null) {
                    val followResponse = response.data as FollowResponse
                    var followResponse1:List<DataItemses>?=null
                    val list1 = ArrayList<DataItemses>()
                    if (followResponse.data!=null) {
                        numbers=0
                        for (i in followResponse.data!!.indices) {
                            if (followResponse.data?.get(i)?.active != null) {
                                if (followResponse.data.get(i).active!!.equals("1")) {
                                    numbers = numbers!! + 1
                                    list1.add(followResponse.data.get(i))
                                    followResponse1 = listOf(followResponse.data.get(i))
                                }
                            }
                        }
                        followAdapter = FollowerAdapter(this, followResponse,this,followResponse1,list1)
                        activityFollowBinding?.rvFollow?.adapter = followAdapter
                    }
                    else {
                        numbers=0
                        for (i in followResponse1!!.indices) {
                            if (followResponse1!!.get(i)?.active != null) {
                                if (followResponse1!!.get(i).active!!.equals("1")) {
                                    numbers = numbers!! + 1
                                    list1.add(followResponse1!!.get(i))
                                    followResponse1 = listOf(followResponse1!!.get(i))
                                }
                            }

                        }
                        followAdapter = FollowerAdapter(this, followResponse,this,followResponse1,list1)
                        activityFollowBinding?.rvFollow?.adapter = followAdapter
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
        }
    }

    override fun onClick() {
        startActivity(SettingActivity.getCallingIntent(this))
    }

    override fun onBlockClick(position: Int, id: String) {

        followViewModel!!.postBlock(
            appPreference.USERID,id
            ).observe(this, Observer { response ->
            if (response?.data != null) {
                val followResponse = response.data as SignupResponse
                if (followResponse.message.equals("Block request sent successfully!")) {
                    showToast(followResponse.message!!)
                    getFollowFollowingList(appPreference.USERID)
                }
            }
            })

    }

    override fun onBlocksClick(position: Int, id: String) {

        followViewModel!!.postBlock(
            appPreference.USERID,id
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val followResponse = response.data as SignupResponse
                if (followResponse.message.equals("Block request sent successfully!")) {
                    showToast(followResponse.message!!)
                    getFollowFollowingList(appPreference.USERID)
                }
            }
        })
    }

    override fun onunfollowClick(position: Int, id: String) {
        followViewModel!!.unfollows(
            appPreference.USERID,id
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val followResponse = response.data as UserProfileResponse
                if (followResponse.message.equals("UnFollow request sent successfully!")) {
                    showToast(followResponse.message!!)
                    getFollowFollowingList(appPreference.USERID)
                }
            }
        })
    }
}