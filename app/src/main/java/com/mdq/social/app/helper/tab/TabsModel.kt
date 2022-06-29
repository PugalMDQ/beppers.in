package com.amilfreight.amilfreight.data.models.tab

import androidx.fragment.app.Fragment

class TabsModel {

    var fragment: Fragment? = null
    var title: String? = null
    var iconResId: Int = 0
    var count: String? = null


    constructor(fragment: Fragment, title: String) {
        this.fragment = fragment
        this.title = title

    }

    constructor(fragment: Fragment, title: String, iconResId: Int) {
        this.fragment = fragment
        this.title = title
        this.iconResId = iconResId
    }

    constructor(fragment: Fragment, title: String, count: String) {
        this.fragment = fragment
        this.title = title
        this.count = count
    }
}
