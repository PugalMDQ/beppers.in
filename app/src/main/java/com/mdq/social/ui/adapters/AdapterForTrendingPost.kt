package com.mdq.social.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
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
import com.mdq.social.app.data.response.recent.DataItem
import com.mdq.social.ui.home.SliderAdapter2
import com.mdq.social.ui.post.PostActivity
import com.mdq.social.ui.profile.ProfileActivity
import org.w3c.dom.Text
import pl.droidsonroids.gif.GifImageView

class AdapterForTrendingPost (var context: Context,var trendingItem:List<DataItem>, var position:Int,var clickManager:ClickManager,var user_id:String): RecyclerView.Adapter<AdapterForTrendingPost.mine>() {
    interface ClickManager {
        fun sharing(postid: String)

        fun onItemLickClick(
            position: Int,
            imageView32: ImageView,
            tvLikeCount: TextView,
            get: DataItem,
            active: String,
            no_of_like: String,
            textView:TextView?,
            gifImageView: GifImageView?
        )
        fun saveBookmark(postid: String, position: Int, userId: String?,who:String)

        fun hide(postid:String,who:String)

        fun onMenuClick(position: Int, userId: String?,star:TextView)

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

        override fun onBindViewHolder(holder: mine, position: Int) {

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
                if (!trendingItem?.get(position)?.tag.isNullOrEmpty()) {

                    context.startActivity(
                        Intent(context, ProfileActivity::class.java).putExtra(
                            "id",
                            trendingItem?.get(position)?.tag
                        )
                    )
                }
            }

            //sharing
            holder.share?.setOnClickListener {
                clickManager.sharing(trendingItem!!.get(position).id.toString())
            }

            if(trendingItem!=null) {
                    if(!trendingItem?.get(position)?.description.isNullOrEmpty()){
                        holder.desc?.visibility=View.VISIBLE
                    }else{
                        holder.desc?.visibility=View.GONE
                    }
                    if(!trendingItem?.get(position)?.taguser.isNullOrEmpty()){
                        holder.tag?.visibility=View.VISIBLE
                        holder.tagedc?.visibility=View.VISIBLE
                        holder.tag?.setText("@"+trendingItem?.get(position)?.taguser!!.toString()+" ")
                    }else{
                        holder.tag?.visibility=View.GONE
                        holder.tagedc?.visibility=View.GONE
                    }
                    holder.comment?.setOnClickListener {
                        context.startActivity(
                            PostActivity.getCallingIntent(
                                context,
                                trendingItem!!.get(position).id!!,
                                trendingItem!!.get(position).gallery!!,
                                trendingItem!!.get(position).user_id!!,
                                trendingItem!!.get(position).user_name!!
                            )
                        )
                    }

                    if(!trendingItem.get(position).active.isNullOrEmpty()) {
                        if (trendingItem.get(position).active!!.equals("1")) {
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

                    if(!trendingItem!!.get(position).type.equals("user")) {
                        if (!trendingItem!!.get(position).address.isNullOrEmpty()) {
                            holder.address?.setText(trendingItem!!.get(position).address!!.toString())
                        }
                    }else{
                        holder.address?.visibility=View.INVISIBLE
                    }

                    if(!trendingItem.get(position).bookmark_active.isNullOrEmpty()) {
                        if (trendingItem.get(position).bookmark_active!!.equals("1")) {
                            holder.Bookmark?.setTag("saved")
                            holder.Bookmark?.setImageResource(R.drawable.ic_select_bookmark_1_gradient)
                        } else {
                            holder.Bookmark?.setTag("Notsaved")
                            holder.Bookmark?.setImageResource(R.drawable.ic_ribbon_button_1)
                        }
                    }else{
                        holder.Bookmark?.setTag("Notsaved")
                        holder.Bookmark?.setImageResource(R.drawable.ic_ribbon_button_1)
                    }
                    holder.like?.setOnClickListener {
                            clickManager.onItemLickClick(
                                position,
                                holder.like!!,holder.Likecount!!,
                                trendingItem?.get(position)!!,
                                if(trendingItem!!.get(position).active!=null){
                                    trendingItem!!.get(position).active!!
                                }
                                else{
                                    "0"
                                },
                                trendingItem!!.get(position).no_of_likes!!,
                                holder.Likecount,
                                holder.heart
                            )
                            var vibration = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT >= 26) {
                                vibration.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                            }
                            else {
                                var vibe: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                                vibe?.vibrate(200)
                            }
                    }

                    holder.name?.setText(trendingItem.get(position)?.user_name)
                    holder.desc?.setText(trendingItem.get(position)?.description)
                    holder.Commentcount?.setText(trendingItem.get(position)?.no_of_comments)
                    holder.Likecount?.setText(trendingItem.get(position)?.no_of_likes)

                    Glide.with(context)
                        .load("http://mdqualityapps.in/profile/" + trendingItem.get(position)?.profile_photo)
                        .into(holder.img!!)

                    //set on click listener
                    holder.img!!.setOnClickListener {
                       context.startActivity(Intent(context,ProfileActivity::class.java).putExtra("id",trendingItem.get(position)?.user_id))
                    }

                    //icon changes for delete and hide
                    if(!trendingItem?.get(position)?.user_id!!.equals(user_id)){
                        holder.delete?.setImageResource(R.drawable.ic_hide_1_gradient)
                        holder.delete?.setPadding(5,5,5,5)

                    }else{
                        holder.Bookmark?.visibility=View.GONE
                    }

                    holder.delete?.setOnClickListener {
                        clickManager.hide(trendingItem?.get(position)?.id!!,"Trending")
                    }

                    if (trendingItem.get(position)?.gallery?.contains(".mp4")!!) {
                        holder.video!!.visibility = View.VISIBLE
                        holder.cardformuteAndUnmute!!.visibility = View.VISIBLE
                        holder.slider!!.visibility = View.GONE
                        holder.video!!.visibility = View.VISIBLE
                        holder.video!!.setVideoURI(
                            Uri.parse(
                                "https://mdqualityapps.in/gallery/" + trendingItem.get(
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
                        if (trendingItem.get(position)?.gallery!!.contains(",")) {
                            val str = trendingItem.get(position)!!.gallery
                            val arr: List<String>? =
                                trendingItem.get(position)!!.gallery?.split(",")
                            for (i in arr!!.indices) {
                                uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + arr.get(i)))
                            }
                        } else {
                            uri?.add(
                                Uri.parse(
                                    "https://mdqualityapps.in/gallery/" + trendingItem.get(
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
            holder.imageView59?.setOnClickListener {
                if (holder.ii == 0) {
                    holder.ii = 1
                    holder.rating?.visibility = View.VISIBLE
                    holder.delete?.visibility = View.VISIBLE
                    holder.share?.visibility = View.VISIBLE
                    if(trendingItem!!.get(position).type?.trim()!!.equals("freelancer")||trendingItem!!.get(position).type?.trim()!!.equals("business")){
                        holder.stare?.visibility = View.VISIBLE
                        clickManager.onMenuClick(position,trendingItem?.get(position).user_id!!,holder.stare!!)
                    }
                } else  {
                    holder.ii = 0
                    holder.delete?.visibility = View.GONE
                    holder.share?.visibility = View.GONE
                    holder.stare?.visibility = View.GONE
                    holder.rating?.visibility = View.GONE
                }
            }

            holder.Bookmark?.setOnClickListener {
                clickManager.saveBookmark(trendingItem!!.get(position).id.toString(),position,trendingItem!!.get(position).user_id!!,"trending")
                if(holder.Bookmark?.getTag()!!.equals("saved")){
                    holder.Bookmark?.setTag("Notsaved")
                    holder.Bookmark?.setImageResource(R.drawable.ic_ribbon_button_1)
                }else{
                    holder.Bookmark?.setTag("saved")
                    holder.Bookmark?.setImageResource(R.drawable.ic_select_bookmark_1_gradient)
                }
            }
        }

        override fun getItemCount(): Int {
            return trendingItem!!.size
        }

        inner class mine(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ii:Int =0

            var img: CircularImageView? = null
            var name: TextView?=null
            var desc: TextView?=null
            var Commentcount: TextView?=null
            var video: VideoView?=null
            var tag: TextView?=null
            var address: TextView?=null
            var tagedc: TextView?=null
            var Likecount: TextView?=null
            var slider: SliderView?=null
            var muteAndUnmute: ImageView?=null
            var like: ImageView?=null
            var comment: ImageView?=null
            var Bookmark: ImageView?=null
            var SingleImage: ImageView?=null
            var cardformuteAndUnmute: CardView?=null
            var imageView59: ImageView?=null
            var delete: ImageView?=null
            var share: ImageView?=null
            var stare: TextView?=null
            var heart: GifImageView?=null
            var rating: TextView?=null

            init {
                img=itemView.findViewById(R.id.imgP)
                cardformuteAndUnmute=itemView.findViewById(R.id.cardformuteAndUnmute)
                muteAndUnmute=itemView.findViewById(R.id.muteAndUnmute)
                desc=itemView.findViewById(R.id.textView95)
                name=itemView.findViewById(R.id.textView92)
                like=itemView.findViewById(R.id.imageView32)
                heart=itemView.findViewById(R.id.heart)
                address=itemView.findViewById(R.id.textView94)
                Bookmark=itemView.findViewById(R.id.imageView34)
                imageView59=itemView.findViewById(R.id.imageView59)
                share=itemView.findViewById(R.id.share)
                stare=itemView.findViewById(R.id.stare)
                rating=itemView.findViewById(R.id.rating)
                SingleImage=itemView.findViewById(R.id.SingleImage)
                delete=itemView.findViewById(R.id.delete)
                tag=itemView.findViewById(R.id.taged)
                tagedc=itemView.findViewById(R.id.tagedc)
                comment=itemView.findViewById(R.id.imageView33)
                Commentcount=itemView.findViewById(R.id.textView97)
                Likecount=itemView.findViewById(R.id.textView96)
                video=itemView.findViewById(R.id.payer1)
                slider=itemView.findViewById(R.id.image_slider)
            }
        }
    }
