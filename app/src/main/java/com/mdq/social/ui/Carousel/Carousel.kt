package com.mdq.social.ui.Carousel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mdq.social.R

class Carousel : AppCompatActivity() {
    val fragments:ArrayList<Fragment> = arrayListOf(Slider1(),Slider2(),Slider3())
     lateinit var tab:TabLayout
     lateinit var adapter:Adapter
    lateinit var view:ViewPager
     companion object {
         fun getCallingIntent(context: Context): Intent {
             return Intent(context, Carousel::class.java)

         }
     }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel)
        adapter= Adapter(fragments,supportFragmentManager)
        view=findViewById(R.id.photos_viewpager)
        tab=findViewById(R.id.tabs)
        view.adapter=adapter
        tab.setupWithViewPager(view)

    }
}