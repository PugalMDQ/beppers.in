package com.mdq.social.app.data.viewmodels.setting

import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.setting.SettingNaviagtor
import javax.inject.Inject

class SettingViewModel @Inject constructor():BaseViewModel<SettingNaviagtor>() {

    fun onClick(state:Int){
        navigator.onClick(state)
    }
}