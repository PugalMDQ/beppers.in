package com.mdq.social.ui.blockaccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.blocklist.DataItem
import com.mdq.social.app.data.response.connectionrequest.ConnectionRequest
import com.mdq.social.app.data.response.connectionrequest.dateItems
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.blockaccount.BlockAccountViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityBlockBinding
import kotlinx.android.synthetic.main.activity_block.*
import kotlinx.android.synthetic.main.activity_block.editTextTextPersonName15

class BlockAccountActivity : BaseActivity<ActivityBlockBinding, BlockAccountNavigator>(),
    BlockAccountNavigator, TextWatcher,BlockAccountAdapter.unBlockClick {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, BlockAccountActivity::class.java)
        }
    }

    private var blockAccountViewModel: BlockAccountViewModel? = null
    private var activityBlockBinding: ActivityBlockBinding? = null
    private var blockAccountAdapter: BlockAccountAdapter? = null
    private var dataItem: List<dateItems>? = null
    private var dataItemList = ArrayList<DataItem>()

    override fun getLayoutId(): Int {
        return R.layout.activity_block
    }

    override fun getViewModel(): BaseViewModel<BlockAccountNavigator> {
        blockAccountViewModel =
            ViewModelProvider(this, viewModelFactory).get(BlockAccountViewModel::class.java)
        return blockAccountViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.blockAccountViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBlockBinding = getViewDataBinding()
        activityBlockBinding?.blockAccountViewModel = blockAccountViewModel
        blockAccountViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        editTextTextPersonName15.addTextChangedListener(this)
        getBlockList()

    }

    private fun getBlockList() {
        blockAccountViewModel!!.getBlockList()
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val blockListResponse = response.data as ConnectionRequest
                    if (blockListResponse != null && blockListResponse?.data != null) {
                        activityBlockBinding!!.textView50.visibility= View.GONE
                        activityBlockBinding!!.imageView58.visibility= View.GONE
                        dataItem=blockListResponse.data
                            blockAccountAdapter = BlockAccountAdapter(this,dataItem,this)
                            rv_block.adapter = blockAccountAdapter
                    } else {
                        rv_block.visibility=View.GONE
                        activityBlockBinding!!.textView50.visibility= View.VISIBLE
                        activityBlockBinding!!.imageView58.visibility= View.VISIBLE
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

        filter(s.toString())
    }

    fun filter(query:String){

        val query=query.toString()

        val newData = ArrayList<dateItems>()
        if(dataItem!=null){

            if(!dataItem.isNullOrEmpty()) {
                for (i in dataItem!!.indices) {
                    if (dataItem?.get(i)?.user_name?.toString()?.trim()?.toLowerCase()!!
                            .contains(query)
                    ) {
                        newData.add(dataItem?.get(i)!!)
                    }
                }
                blockAccountAdapter = BlockAccountAdapter(this,newData,this)
                rv_block.adapter = blockAccountAdapter
            }
        }
    }

    override fun onClick() {
        onBackPressed()
    }

    override fun unBlockClick(user_id: String) {
        editTextTextPersonName15.setText("")
        blockAccountViewModel!!.unBlock(user_id)
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val blockListResponse = response.data as SignupResponse
                    if (blockListResponse .message.equals("Block request sent successfully!")) {
                        showToast("unblocked")
                        getBlockList()
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }
}
