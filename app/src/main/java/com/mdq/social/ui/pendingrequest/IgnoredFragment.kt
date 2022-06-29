package com.mdq.social.ui.pendingrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.R

/**
 * A simple [Fragment] subclass.
 * Use the [IgnoredFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IgnoredFragment : Fragment() {
    var recyclerView: RecyclerView? =null
    var layoutmanagers: LinearLayoutManager? =null
    var adapters:IgnoredAdapter ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val views:View=inflater.inflate(R.layout.fragment_ignored,container,false)

        recyclerView=views.findViewById(R.id.rv)
        layoutmanagers=LinearLayoutManager(context)

        return views
    }

}