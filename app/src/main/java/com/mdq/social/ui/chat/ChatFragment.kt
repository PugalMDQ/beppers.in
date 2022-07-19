package com.mdq.social.ui.chat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.response.livechat.ChatCount
import com.mdq.social.app.data.response.livechat.count
import com.mdq.social.app.data.response.livechatprofile.DataItem
import com.mdq.social.app.data.response.livechatprofile.LiveChatProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.chat.ChatViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentChatBinding
import com.mdq.social.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseFragment<FragmentChatBinding, ChatNavigator>(),
    TextWatcher, RecentChatAdapter.click {
    private var chatViewModel: ChatViewModel? = null
    var fragmentChatBinding: FragmentChatBinding? = null
    private var recentChatAdapter: RecentChatAdapter? = null
    var datas = ArrayList<DataItem>()
    var countdatas: List<count>? = null
    var preferenceManager: PreferenceManager? = null
    var chat: ChatNavigator? = null
    var textview1: TextView? = null
    var handler: Handler? = null
    var stop: Boolean = true
    var values: Boolean = false
    public var strtext:String ? =null

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat
    }

    interface click {
        fun clicks(text: TextView)
    }

    override fun getViewModel(): BaseViewModel<ChatNavigator> {
        chatViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)
        return chatViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.chatViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentChatBinding = getViewDataBinding()
        fragmentChatBinding?.chatViewModel = chatViewModel

        try {
            strtext = requireArguments().getString("edttext")
            if(!strtext.isNullOrEmpty()){
                values=true
            }
        }catch (e:Exception){

        }

        if (!appPreference.ADMINBLOCK.equals("1")) {
            fetchUsers()
        } else {
            fragmentChatBinding!!.BlockImage.visibility = View.VISIBLE
            fragmentChatBinding!!.blockText.visibility = View.VISIBLE
        }
        imageView.setOnClickListener {
            startActivity(Intent(activity, HomeActivity::class.java))
        }
        fragmentChatBinding?.textView25?.addTextChangedListener(this)
    }

    private fun filteres(newText: String) {
        val newData = ArrayList<DataItem>()
        if (datas != null) {
            try {
                if (!datas!!.isNullOrEmpty()) {
                    for (i in datas!!.indices) {

                        if (datas?.get(i)?.from_username?.toString()?.trim()
                                ?.toLowerCase()!!
                                .contains(newText)
                        ) {
                            newData.add(datas?.get(i)!!)
                        } else
                            if (datas?.get(i)?.to_username?.toString()?.trim()
                                    ?.toLowerCase()!!
                                    .contains(newText)
                            ) {
                                newData.add(datas?.get(i)!!)
                            }
                    }

                }
                recentChatAdapter =
                    RecentChatAdapter(
                        requireContext(),
                        appPreference.USERID,
                        newData,
                        appPreference.USERNAME,
                        appPreference.FIREBASEUSERID,
                        this,
                        countdatas,
                        values,
                        strtext

                        )
                fragmentChatBinding?.rvRechat?.adapter = recentChatAdapter
            } catch (e: Exception) {
            }
        }
    }

    fun fetchUsers() {

        try {
            chatViewModel?.getChatList()?.observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val responses = response.data as LiveChatProfileResponse
                    datas.clear()
                    for (i in responses.data!!.indices) {
                        if (responses.data.get(i)?.admin_block!!.equals("0") &&
                            responses.data.get(i)?.user_block!!.equals("0") &&
                            responses.data.get(i)?.deactivate!!.equals("0") &&
                            !responses.data.get(i)?.chat_block!!.trim().equals(appPreference.USERID.trim())
                        ) {
                            datas.add(responses.data.get(i)!!)
                        }
                    }
                    fetchCount()
                    if (stop) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            fetchUsers()
                        }, 5000)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })

        } catch (e: Exception) {

        }
    }

    fun fetchCount() {
        try {
            chatViewModel?.getChatCount()?.observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val response = response.data as ChatCount
                    countdatas = response.data
                    if (datas != null) {
                        recentChatAdapter = RecentChatAdapter(
                            requireContext(),
                            appPreference.USERID,
                            datas!!,
                            appPreference.USERNAME,
                            appPreference.FIREBASEUSERID,
                            this,
                            response.data,
                            values,strtext
                        )
                        fragmentChatBinding?.rvRechat?.adapter = recentChatAdapter
                    }
                }
            })
        } catch (e: Exception) {
        }
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(requireContext())
        }
        return preferenceManager
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        filteres(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun click(textview: TextView, text: String) {
        textview1 = TextView(requireActivity())
        textview1 = textview
    }

    override fun onStop() {
        super.onStop()
        stop = false;
    }

    override fun onPause() {
        super.onPause()
        stop = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stop = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stop = false
    }
}