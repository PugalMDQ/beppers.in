package com.mdq.social.ui.listOfPost

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.siyamed.shapeimageview.CircularImageView
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.mdq.social.R
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.ui.home.SliderAdapter2
import com.mdq.social.ui.post.PostActivity
import com.mdq.social.ui.profile.ProfileActivity
import pl.droidsonroids.gif.GifImageView

class AdapterForList(
    var context: Context,
    var recentItem: UserProfileResponse,
    var position: Int,
    var likes: like,
    var user_id: String,
    var appPreferenceuser_id: String,
    var clicks:clickManager
): RecyclerView.Adapter<AdapterForList.mine>() {
    var ii:Int =0

    interface clickManager{
        fun delete(postid:String)
        fun hide(postid:String,who:String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mine {

        return mine(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_home,
                parent,
                false
            )
        )
    }

    interface like{
        fun like(id:String,user_id:String,image:ImageView,no_of_like:String,position: Int,active:String,textView:TextView?,gifImageView: GifImageView?)
    }

    override fun onBindViewHolder(holder: mine, position: Int) {

        holder.bookmark?.visibility=View.GONE
        holder.muteAndUnmute?.setOnClickListener {
            if (holder.muteAndUnmute?.getTag()!!.equals("work")) {
                holder.muteAndUnmute!!.setImageResource(R.drawable.ic_pause__1_)
                holder.video!!.start()
                holder.muteAndUnmute!!.setTag("notwork")
            } else {
                holder.muteAndUnmute!!.setImageResource(R.drawable.ic_play__1_)
                holder.video!!.pause()
                holder.muteAndUnmute!!.setTag("work")
            }
        }



        holder.tag?.setOnClickListener {
            if (!recentItem?.data?.get(position)?.tag.isNullOrEmpty()) {
                context.startActivity(
                    Intent(context, ProfileActivity::class.java).putExtra(
                        "id",
                        recentItem?.data?.get(position)?.tag
                    )
                )
            }
        }

        holder.menu?.setOnClickListener {
            if (ii == 0) {
                ii = 1
                holder!!.share?.visibility = View.VISIBLE

                if (appPreferenceuser_id.equals(user_id)) {
                    holder!!.delete?.visibility = View.VISIBLE
                    holder.delete?.setOnClickListener {
                        clicks.delete(recentItem?.data?.get(position)?.id!!)
                    }
                }

            } else if (ii == 1) {
                ii = 0
                holder!!.delete?.visibility = View.GONE
                holder!!.share?.visibility = View.GONE
                holder!!.star?.visibility = View.GONE
            }
        }

            if(!recentItem?.data?.get(position)?.description.isNullOrEmpty()){
                holder.desc?.visibility=View.VISIBLE
            }else{
                holder.desc?.visibility=View.GONE
            }
            if(!recentItem?.data?.get(position)?.taguser.isNullOrEmpty()){
                holder.tag?.visibility=View.VISIBLE
                holder.tagedc?.visibility=View.VISIBLE
                holder.tag?.setText("@"+recentItem?.data?.get(position)?.taguser!!.toString()+" ")
            }else{
                holder.tag?.visibility=View.GONE
                holder.tagedc?.visibility=View.GONE
            }

        holder.comment?.setOnClickListener {
            context.startActivity(
                PostActivity.getCallingIntent(
                    context,
                    recentItem!!.data?.get(position)?.id!!,
                    recentItem!!.data?.get(position)?.gallery!!,
                    recentItem!!.data?.get(position)?.user_id!!,
                    recentItem!!.data?.get(position)?.user_name!!

                )
            )
        }

        if(!recentItem.data?.get(position)?.active.isNullOrEmpty()) {
            if (recentItem.data?.get(position)?.active!!.equals("1")) {
                holder.like?.setTag("Liked")
                holder.like?.setImageResource(R.drawable.ic_heart_1fill)
            } else {
                holder.like?.setTag("Unliked")
                holder.like?.setImageResource(R.drawable.ic_heart_1__1_)
            }
        }else{
            holder.like?.setTag("Unliked")
            holder.like?.setImageResource(R.drawable.ic_heart_1__1_)
        }

            if (recentItem != null) {
                holder.name?.setText(recentItem.data!!.get(position)?.user_name)
                holder.desc?.setText(recentItem.data!!.get(position)?.description)
                holder.Commentcount?.setText(recentItem.data!!.get(position)?.no_of_comments)
                holder.Likecount?.setText(recentItem.data!!.get(position)?.no_of_likes)

                Glide.with(context)
                    .load("http://mdqualityapps.in/profile/" + recentItem.data!!.get(position)?.profile_photo)
                    .into(holder.img!!)

                if (recentItem.data?.get(position)?.gallery?.contains(".mp4")!!) {
                    holder.video!!.visibility = View.VISIBLE
                    holder.cardformuteAndUnmute!!.visibility = View.VISIBLE
                    holder.slider!!.visibility = View.GONE
                    holder.video!!.visibility = View.VISIBLE
                    holder.video!!.setVideoURI(
                        Uri.parse(
                            "https://mdqualityapps.in/gallery/" + recentItem.data?.get(
                                position
                            )!!.gallery
                        )
                    )
                    holder.video!!.seekTo(1)
                    holder.video!!.setOnPreparedListener { mp ->
                        mp!!.isLooping = true
                    }
                } else {
                    holder.cardformuteAndUnmute!!.visibility = View.GONE
                    holder.video!!.visibility = View.INVISIBLE
                    holder.slider!!.visibility = View.VISIBLE
                    holder.video!!.visibility = View.GONE
                    holder.video!!.visibility = View.GONE
                    var sliderAdapter: SliderAdapter2? = null
                    var uri: ArrayList<Uri>? = ArrayList()
                    if (recentItem.data?.get(position)?.gallery!!.contains(",")) {
                        val str = recentItem.data?.get(position)!!.gallery
                        val arr: List<String>? =
                            recentItem.data?.get(position)!!.gallery?.split(",")
                        for (i in arr!!.indices) {
                            uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + arr.get(i)))
                        }
                    } else {
                        uri?.add(
                            Uri.parse(
                                "https://mdqualityapps.in/gallery/" + recentItem.data?.get(
                                    position
                                )!!.gallery
                            )
                        )
                    }
                    if(uri!!.size>1){
                        holder.SingleImage?.visibility=View.GONE
                        holder.slider!!.visibility=View.VISIBLE

                        sliderAdapter = SliderAdapter2(context, uri!!)
                        holder.slider!!.setSliderAdapter(sliderAdapter!!)
                        holder.slider!!.setIndicatorAnimation(IndicatorAnimationType.WORM)
                        holder.slider!!.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)

                    }else{
                        holder.SingleImage?.visibility=View.VISIBLE
                        holder.slider!!.visibility=View.GONE
                        Glide.with(context).load(uri[0]).into(holder.SingleImage!!)
                    }
                }
            }

        holder.like?.setOnClickListener {
            likes.like(recentItem.data?.get(position)?.id!!,recentItem.data?.get(position)?.user_id!!,
                holder.like!!,recentItem.data?.get(position)?.no_of_likes!!,position,
                if(recentItem!!.data?.get(position)?.active!=null){
                    recentItem!!.data?.get(position)?.active!!
                }
                else{
                    "0"
                },holder.Likecount
            ,holder.heart
            )
        }
    }

    override fun getItemCount(): Int {
        return recentItem!!.data!!.size
    }

    inner class mine(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var img: CircularImageView? = null
        var name:TextView?=null
        var desc:TextView?=null
        var Commentcount:TextView?=null
        var video:VideoView?=null
        var tag: TextView?=null
        var tagedc: TextView?=null
        var Likecount:TextView?=null
        var slider:SliderView?=null
        var muteAndUnmute:ImageView?=null
        var like:ImageView?=null
        var star:TextView?=null
        var bookmark:ImageView?=null
        var SingleImage:ImageView?=null
        var share:ImageView?=null
        var delete:ImageView?=null
        var menu:ImageView?=null
        var comment:ImageView?=null
        var heart:GifImageView?=null
        var cardformuteAndUnmute:CardView?=null

    init {
        img=itemView.findViewById(R.id.imgP)
        cardformuteAndUnmute=itemView.findViewById(R.id.cardformuteAndUnmute)
        muteAndUnmute=itemView.findViewById(R.id.muteAndUnmute)
        desc=itemView.findViewById(R.id.textView95)
        name=itemView.findViewById(R.id.textView92)
        tag=itemView.findViewById(R.id.taged)
        heart=itemView.findViewById(R.id.heart)
        tagedc=itemView.findViewById(R.id.tagedc)
        like=itemView.findViewById(R.id.imageView32)
        bookmark=itemView.findViewById(R.id.imageView34)
        SingleImage=itemView.findViewById(R.id.SingleImage)
        star=itemView.findViewById(R.id.stare)
        share=itemView.findViewById(R.id.share)
        delete=itemView.findViewById(R.id.delete)
        menu=itemView.findViewById(R.id.imageView59)
        comment=itemView.findViewById(R.id.imageView33)
        Commentcount=itemView.findViewById(R.id.textView97)
        Likecount=itemView.findViewById(R.id.textView96)
        video=itemView.findViewById(R.id.payer1)
        slider=itemView.findViewById(R.id.image_slider)
    }
}
}