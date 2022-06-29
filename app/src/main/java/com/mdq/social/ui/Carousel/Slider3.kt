package com.mdq.social.ui.Carousel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mdq.social.R
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.ui.login.LoginActivity

class Slider3 : Fragment() {

    lateinit var appPreference:AppPreference
    lateinit var text:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_slider3, container, false)

        text= view.findViewById(R.id.textView5)


        text.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                    val intent = Intent(context,LoginActivity::class.java)
                    startActivity(intent)
                }

        })
        return view
    }

}