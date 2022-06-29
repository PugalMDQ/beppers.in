package com.mdq.social.ui.addpost

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vkart.phoneauth.adapters.SelectedImageAdapter
import com.mdq.social.databinding.ActivitySelctedImagesBinding
import java.util.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
class selctedImages : AppCompatActivity() {
    var mArrayUri: ArrayList<String>? = null
    var uri:ArrayList<Uri>?=null
    var activitySelectImageBinding:ActivitySelctedImagesBinding?=null
    var adapters:SelectedImageAdapter?=null
    var HorizontalLayout:LinearLayoutManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySelectImageBinding= ActivitySelctedImagesBinding.inflate(layoutInflater)
        setContentView(activitySelectImageBinding!!.root)
        mArrayUri= ArrayList<String>()
        uri= ArrayList<Uri>()
        var intent=intent.extras
        mArrayUri=intent?.getStringArrayList("dd") as ArrayList<String>?
        if(mArrayUri!=null){
            for (i in 0 until mArrayUri!!.size) {
                uri!!.addAll(listOf(Uri.parse(mArrayUri!!.get(i))))
            }
        }

        HorizontalLayout = LinearLayoutManager(
            this@selctedImages,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapters= SelectedImageAdapter(this@selctedImages,uri!!)
        activitySelectImageBinding!!.rv.adapter=adapters
        activitySelectImageBinding!!.rv.layoutManager=HorizontalLayout

    }
}