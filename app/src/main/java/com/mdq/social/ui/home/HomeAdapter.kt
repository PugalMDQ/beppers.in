package com.mdq.social.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.mdq.social.R
import com.mdq.social.app.data.response.recent.DataItem
import com.mdq.social.app.data.viewmodels.home.HomeViewModel
import com.mdq.social.databinding.ItemHomeBinding
import com.mdq.social.ui.post.PostActivity
import com.mdq.social.ui.profile.ProfileActivity
import android.annotation.SuppressLint
import android.os.*
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener


class HomeAdapter(val context: Context, var clickManager: ClickManager,var homeViewModel: HomeViewModel,var user_id:String) : AAH_VideosAdapter(){

    private var recentItem: List<DataItem>? = null

    init {
        recentItem = ArrayList()
    }

    interface ClickManager {
        fun onItemLickClick(position: Int, imageView32: ImageView, tvLikeCount: TextView, get: DataItem,active:String,no_of_like:String)
        fun onItemSubscribeClick(position: Int, get: DataItem, imageView: ImageView)
        fun onItemProfileClick(position: Int, get: DataItem)
        fun onShareClick(position: Int, get: DataItem)
        fun onMenuClick(position: Int, get: DataItem,star:TextView)
        fun onHideClick(postid: String, position: Int, userId: String?)
        fun saveBookmark(postid: String, position: Int, userId: String?)
    }

    fun setHomeAdapter(itemList: List<DataItem>?) {

        if (itemList == null) {
            return
        }

        recentItem = itemList
        notifyDataSetChanged()

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AAH_CustomViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_home,
                parent,
                false
            )
        )
    }

    @SuppressLint("ServiceCast")
    override fun onBindViewHolder(holder: AAH_CustomViewHolder, @SuppressLint("RecyclerView") position: Int) {
        super.onBindViewHolder(holder, position)
        val item: DataItem = recentItem!!.get(position)
        val binding: MyViewHolder = holder as MyViewHolder

        holder.getBinding().muteAndUnmute?.setOnClickListener {
            if (holder.getBinding().muteAndUnmute?.getTag().equals("work")) {
                holder.getBinding().muteAndUnmute!!.setImageResource(R.drawable.ic_pause__1_)
                holder.getBinding().payer1!!.start()
                holder.getBinding().muteAndUnmute.setTag("notwork")
            }else {
                holder.getBinding().muteAndUnmute!!.setImageResource(R.drawable.ic_play__1_)
                holder.getBinding().payer1!!.pause()
                holder.getBinding().muteAndUnmute.setTag("work")
            }
        }
        if (item.gallery?.contains(".mp4")!!) {
            holder.getBinding().cardformuteAndUnmute!!.visibility=View.VISIBLE
            holder.getBinding().imageSlider!!.visibility=View.GONE
            holder.getBinding().payer1.visibility=View.VISIBLE
            holder.getBinding().payer1.setVideoURI(Uri.parse("https://mdqualityapps.in/gallery/"+ item.gallery))
            holder.getBinding().payer1.seekTo(1)
            holder.getBinding().payer1.setOnPreparedListener(OnPreparedListener { mp -> //Get your video's width and height
                val videoWidth = mp.videoWidth
                val videoHeight = mp.videoHeight

                val videoViewWidth: Int = holder.getBinding().payer1.getWidth()
                val videoViewHeight: Int = holder.getBinding().payer1.getHeight()
                val xScale = videoViewWidth.toFloat() / videoWidth
                val yScale = videoViewHeight.toFloat() / videoHeight

                val scale = Math.min(xScale, yScale)
                val scaledWidth = scale * videoWidth
                val scaledHeight = scale * videoHeight

                val layoutParams: ViewGroup.LayoutParams = holder.getBinding().payer1.getLayoutParams()
                layoutParams.width = scaledWidth.toInt()
                layoutParams.height = scaledHeight.toInt()
                holder.getBinding().payer1.setLayoutParams(layoutParams)

            })
        }

        else {
            holder.getBinding().cardformuteAndUnmute!!.visibility = View.GONE
            holder.getBinding().imageSlider!!.visibility = View.VISIBLE
            holder.getBinding().payer1.visibility = View.GONE
            var sliderAdapter: SliderAdapter2? = null
            var uri: ArrayList<Uri>? = ArrayList()
            if (item.gallery.contains(",")) {
                val str = item.gallery
                val arr: List<String> = item.gallery?.split(",")
                for (i in arr.indices) {
                    uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + arr.get(i)))
                }
            } else {
                uri?.add(Uri.parse("https://mdqualityapps.in/gallery/" + item.gallery))
            }
            if (uri!!.size > 1) {
                holder.getBinding().SingleImage.visibility = View.GONE
                holder.getBinding().imageSlider.visibility = View.VISIBLE
                sliderAdapter = SliderAdapter2(context, uri!!)
                holder.getBinding().imageSlider!!.setSliderAdapter(sliderAdapter!!)
                holder.getBinding().imageSlider!!.setIndicatorAnimation(IndicatorAnimationType.WORM)
                holder.getBinding().imageSlider!!.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
            } else {
                holder.getBinding().SingleImage.visibility = View.VISIBLE
                holder.getBinding().imageSlider.visibility = View.GONE
                Glide.with(context).load(uri[0]).into(holder.getBinding().SingleImage)
            }
    }

        holder.getBinding().taged.setOnClickListener {
            if (!item.tag.isNullOrEmpty()) {
                context.startActivity(
                    Intent(context, ProfileActivity::class.java).putExtra(
                        "id",
                        item.tag
                    )
                )
            }
        }

        if(!item.description.isNullOrEmpty()){
            holder.getBinding()?.textView95.visibility=View.VISIBLE
        }else{
            holder.getBinding()?.textView95.visibility=View.GONE
        }
            if(!item.taguser.isNullOrEmpty()){
                holder.getBinding()?.taged.visibility=View.VISIBLE
                holder.getBinding()?.tagedc.visibility=View.VISIBLE
                holder.getBinding()?.taged?.setText("@"+item.taguser!!.toString()+" ")
            }else{
                holder.getBinding()?.taged.visibility=View.GONE
                holder.getBinding()?.tagedc.visibility=View.GONE
            }

        if(!item.type.equals("user")) {
            if (!item.address.isNullOrEmpty()) {
                holder.getBinding().textView94.setText(item.address!!.toString())
            }
        }else{
            holder.getBinding().textView94.visibility=View.INVISIBLE
        }

        if(!item.user_id!!.equals(user_id)){
            holder.getBinding().delete.setImageResource(R.drawable.ic_hide_1_gradient)
            holder.getBinding().delete.setPadding(5,5,5,5)
        }

        if(!item.active.isNullOrEmpty()) {
            if (item.active!!.equals("1")) {
                holder.getBinding().imageView32.setTag("Liked")
                binding.getBinding().imageView32.setImageResource(R.drawable.ic_heart_1fill)
            } else {
                holder.getBinding().imageView32.setTag("Unliked")
                binding.getBinding().imageView32.setImageResource(R.drawable.ic_heart_1__1_)
            }
        }else{
            holder.getBinding().imageView32.setTag("Unliked")
            binding.getBinding().imageView32.setImageResource(R.drawable.ic_heart_1__1_)
        }

        if(!item.bookmark_active.isNullOrEmpty()) {
            if (item.bookmark_active!!.equals("1")) {
                holder.getBinding().imageView34.setTag("saved")
                binding.getBinding().imageView34.setImageResource(R.drawable.ic_select_bookmark_1_gradient)
            } else {
                holder.getBinding().imageView34.setTag("Notsaved")
                binding.getBinding().imageView34.setImageResource(R.drawable.ic_ribbon_button_1)
            }
        }else{
            holder.getBinding().imageView34.setTag("Notsaved")
            binding.getBinding().imageView34.setImageResource(R.drawable.ic_ribbon_button_1)
        }

        binding.getBinding().imageView32.setOnClickListener {

            clickManager.onItemLickClick(
                position,
                binding.getBinding().imageView32!!, binding.getBinding().textView96!!,
                recentItem?.get(position)!!,

                 if(recentItem!!.get(position).active!=null){
                     recentItem!!.get(position).active!!
                 }
                 else{
                     "0"
                 },
                recentItem!!.get(position).no_of_likes!!
            )
            var vibration = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {

                vibration.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))

            }
            else {
                var vibe: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                vibe?.vibrate(200)
            }
        }

        binding.getBinding().imageView34.setOnClickListener {
            clickManager.onItemSubscribeClick(position, recentItem?.get(position)!!, binding.getBinding()?.imageView34!!)
        }
        binding.getBinding().imgP.setOnClickListener {
            clickManager.onItemProfileClick(position, recentItem?.get(position)!!)
        }
        if(item.user_name!=null) {
            holder.getBinding().textView92.setText(item.user_name!!.toString())
        }
        binding.getBinding().let { setProfile(it, item) }

        binding.getBinding().recentItem = item

        holder.getBinding().textView95?.setText(item.description!!.toString())

        if(! item.no_of_likes!!.toString().contains("-")){
        holder.getBinding().textView96.setText(item.no_of_likes!!.toString())
        }

        holder.getBinding().textView97.setText(item.no_of_comments!!.toString())

        holder.getBinding().delete.setOnClickListener {
            clickManager.onHideClick(recentItem!!.get(position).id.toString(),position,recentItem!!.get(position).user_id!!)
        }

        holder.getBinding().imageView34.setOnClickListener {
            clickManager.saveBookmark(recentItem!!.get(position).id.toString(),position,recentItem!!.get(position).user_id!!)
            if(holder.getBinding().imageView34.getTag().equals("saved")){
                holder.getBinding().imageView34.setTag("Notsaved")
                binding.getBinding().imageView34.setImageResource(R.drawable.ic_ribbon_button_1)
            }else{
                holder.getBinding().imageView34.setTag("saved")
                binding.getBinding().imageView34.setImageResource(R.drawable.ic_select_bookmark_1_gradient)
            }
        }
    }

    override fun getItemCount(): Int {
        return recentItem!!.size
    }

    inner class MyViewHolder(itemView: View) : AAH_CustomViewHolder(itemView), View.OnClickListener {
        private var binding: ItemHomeBinding? = null
        var ii:Int =0

        init {

            binding = DataBindingUtil.bind(itemView)
            binding!!.imageView59.setOnClickListener(this)
            binding!!.imageView33.setOnClickListener(this)
            binding!!.imgP.setOnClickListener(this)
            binding!!.imageView34.setOnClickListener(this)

        }

        fun getBinding(): ItemHomeBinding {
            return binding!!
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.imageView34 ->{
                    binding!!.imageView34.setImageResource(R.drawable.ic_select_bookmark_1_gradient)
                }
                R.id.imageView33 ->{
                    context.startActivity(
                        PostActivity.getCallingIntent(
                            context,
                            recentItem!!.get(bindingAdapterPosition).id!!,
                            recentItem!!.get(bindingAdapterPosition).gallery!!,
                            recentItem!!.get(bindingAdapterPosition).user_id!!,
                            recentItem!!.get(bindingAdapterPosition).user_name!!
                        )
                    )
                }
                R.id.framePlayerView -> {
                    context.startActivity(
                            PostActivity.getCallingIntent(
                                    context,
                                    recentItem!!.get(bindingAdapterPosition).id!!,
                                    recentItem!!.get(bindingAdapterPosition).gallery!!,
                                recentItem!!.get(bindingAdapterPosition).user_id!!,
                                recentItem!!.get(bindingAdapterPosition).user_name!!
                            )
                    )
                }

                R.id.imageView29 -> {
                    clickManager.onShareClick(bindingAdapterPosition, recentItem?.get(bindingAdapterPosition)!!)
                }

                R.id.imageView59 -> {
                    if(ii==0){
                        ii=1
                    binding!!.delete.visibility=View.VISIBLE
                    binding!!.share.visibility=View.VISIBLE
                        if(recentItem?.get(absoluteAdapterPosition)?.type.equals("freelancer") || recentItem?.get(absoluteAdapterPosition)?.type.equals("business")) {
                            binding!!.stare.visibility = View.VISIBLE
                            clickManager.onMenuClick(bindingAdapterPosition,recentItem?.get(bindingAdapterPosition)!!,getBinding().rating)
                        }
                    }
                    else if(ii==1){
                        ii=0
                        binding!!.delete.visibility=View.GONE
                        binding!!.share.visibility=View.GONE
                        binding!!.stare.visibility=View.GONE
                    }
                }
                R.id.imgP -> {
                    context.startActivity(Intent( context,ProfileActivity::class.java))
                }
            }
        }
    }

    private fun setProfile(binding: ItemHomeBinding, item: DataItem) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.noprofile)
            .error(R.drawable.noprofile)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        Glide.with(context)
            .load("http://mdqualityapps.in/profile/"+item.profile_photo)
            .apply(options)
            .into(binding.imgP)
    }
}


