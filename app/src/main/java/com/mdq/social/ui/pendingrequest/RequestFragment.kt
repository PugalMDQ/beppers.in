package com.mdq.social.ui.pendingrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R

class RequestFragment : Fragment() {

    var recyclerView:RecyclerView? =null
    var layoutmanagers:LinearLayoutManager? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View=inflater.inflate(R.layout.fragment_request,container,false)


        return view

    }
}