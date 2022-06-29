package com.mdq.social.ui.individual

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.individual.IndividualViewModel
import com.mdq.social.app.data.viewmodels.profile.ProfileViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityIndividualUpdateBinding
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.utils.UiUtils
import kotlinx.android.synthetic.main.item_for_toast.*

class IndividualActivity : BaseActivity<ActivityIndividualUpdateBinding, IndividualNavigator>(),

    IndividualNavigator {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, IndividualActivity::class.java)
        }
    }

    private var activityIndividualUpdateBinding: ActivityIndividualUpdateBinding? = null
    private var individualViewModel: IndividualViewModel? = null
    private var profileViewModel: ProfileViewModel? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_individual_update
    }

    override fun getViewBindingVarible(): Int {
        return BR.individualViewModel
    }

    override fun getViewModel(): BaseViewModel<IndividualNavigator> {
        individualViewModel =
            ViewModelProvider(this, viewModelFactory).get(IndividualViewModel::class.java)
        return individualViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

//        cardElevation()
        profileViewModel =
            ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        activityIndividualUpdateBinding = getViewDataBinding()
        activityIndividualUpdateBinding?.individualViewModel = individualViewModel
        individualViewModel?.navigator = this

        activityIndividualUpdateBinding?.imageView?.setOnClickListener {
            onBackPressed()
        }
        activityIndividualUpdateBinding?.textView2?.setOnClickListener {
            updateClick()
        }
        getProfileDetail()

        activityIndividualUpdateBinding?.rdoMale?.setOnClickListener {
            individualViewModel?.gender?.set("Male")
        }
        activityIndividualUpdateBinding?.rdoFemale?.setOnClickListener {
            individualViewModel?.gender?.set("female")
        }
        activityIndividualUpdateBinding?.rdoTrans?.setOnClickListener {
            individualViewModel?.gender?.set("trans")
        }


    }

    private fun updateClick() {
        individualViewModel!!.UpdateIndividual().observe(this,
            { response ->
                if (response?.data != null) {
                    val updateForBusiness = response.data as SignupResponse
                    if (updateForBusiness.error.equals("false")) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        showToast(updateForBusiness!!.message.toString())
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun cardElevation() {
        activityIndividualUpdateBinding!!.editTextTextPersonName2.setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            15F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                    }

                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activityIndividualUpdateBinding!!.editTextTextPersonName5.setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            15F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                    }

                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activityIndividualUpdateBinding!!.editTextTextPersonName6.setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            15F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                    }

                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activityIndividualUpdateBinding!!.editTextTextPersonName7.setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activityIndividualUpdateBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            15F
                    }

                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun toast() {
        val dialoglogout = Dialog(this, R.style.dialog_center)
        dialoglogout.setCancelable(false)
        dialoglogout.setContentView(com.mdq.social.R.layout.item_for_toast)
        val textView23 = dialoglogout.ToastText
        val ok = dialoglogout.Ok
        textView23.setText("Already Mobile Number/userName Exists!")
        ok.setOnClickListener {
            dialoglogout?.dismiss()
        }
        dialoglogout.show()
    }

    override fun backOn() {
        onBackPressed()
    }

    private fun getProfileDetail() {
        individualViewModel!!.getUserProfileDetails(appPreference.USERID.toString())
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val userProfileResponse = response.data as UserProfileDetailResponse
                    activityIndividualUpdateBinding?.editTextTextPersonName?.setText(
                        userProfileResponse.data?.get(0)?.name
                    )
                    activityIndividualUpdateBinding?.editTextTextPersonUserName?.setText(
                        userProfileResponse.data?.get(0)?.user_name
                    )
                    activityIndividualUpdateBinding?.editTextTextPersonName2?.setText(
                        userProfileResponse.data?.get(0)?.email
                    )
                    activityIndividualUpdateBinding?.editTextTextPersonName5?.setText(
                        userProfileResponse.data?.get(0)?.password
                    )

                    activityIndividualUpdateBinding?.editTextdesc?.setText(
                        userProfileResponse.data?.get(0)?.description
                    )

                    activityIndividualUpdateBinding?.editTextTextPersonName7?.setText(
                        userProfileResponse.data?.get(0)?.mobile
                    )
                    activityIndividualUpdateBinding?.editTextTextPersonName6?.setText(
                        userProfileResponse.data?.get(0)?.dob
                    )
                    if(userProfileResponse.data?.get(0)?.gender!!.contains("Male")){
                        activityIndividualUpdateBinding?.rdoMale?.isChecked=true
                        individualViewModel?.gender?.set("Male")
                    }
                    if(userProfileResponse.data?.get(0)?.gender!!.contains("female")){
                        activityIndividualUpdateBinding?.rdoFemale?.isChecked=true
                        individualViewModel?.gender?.set("female")
                    }
                    if(userProfileResponse.data?.get(0)?.gender!!.contains("trans")){
                        activityIndividualUpdateBinding?.rdoTrans?.isChecked=true
                        individualViewModel?.gender?.set("trans")
                    }
                }
            })
    }


    override fun updateOn() {

    }

    override fun dob() {
        UiUtils.datePickerDialog(
            this,
            System.currentTimeMillis(),
            0,
            object : UiUtils.ClickManager {
                override fun onDatePickDialog(
                    year: Int,
                    monthOfYear: Int,
                    dayofMonth: Int
                ) {
                    val day =
                        if (dayofMonth > 9) dayofMonth.toString() + "" else "0$dayofMonth"

                    val month =
                        if ((monthOfYear + 1) > 9) (monthOfYear + 1).toString() + "" else "0" + (monthOfYear + 1)
                    var dob = "$day-$month-$year"
                    individualViewModel?.dod?.set(dob)

                }
            })
    }
}