package com.mdq.social.ui.home

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mdq.social.R

/**
 * Example using Glide for load images into ImageView
 */
fun ImageView.loadImage(url: String, thumbnailUrl: String) {

    Glide
        .with(this)
        .load(url)
        .centerCrop() // or other transformation: fitCenter(), circleCrop(), etc
        .thumbnail(Glide.with(this).load(thumbnailUrl))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_logo)
        .transform(RoundedCorners(8))
        .error(android.R.drawable.ic_delete)
        .into(this)

}