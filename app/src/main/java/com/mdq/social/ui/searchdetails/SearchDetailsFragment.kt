package com.mdq.social.ui.searchdetails


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItemes
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItems
import com.mdq.social.app.data.response.getshopAlbumDetails.NewPostsearchDeailResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.UserSearchDetailResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.searchdetails.SearchDetailsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentSearchDetailsBinding
import com.mdq.social.ui.notification.NotificationActivity
import com.mdq.social.ui.signupfreelancer.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_search_details.*
import kotlinx.android.synthetic.main.nav_home_fragment.*

class SearchDetailsFragment : BaseFragment<FragmentSearchDetailsBinding, SearchDetailsNavigator>(),
    CategoryAdapter.ClickManager, TextWatcher, View.OnClickListener, VideoAdapter.postClick {

    private var fragmentSearchDetailsBinding: FragmentSearchDetailsBinding? = null
    private var searchDetailsViewModel: SearchDetailsViewModel? = null
    private var galleryAdapter: VideoAdapter? = null
    private var profileSearchAdapter: ProfileSearchAdapter? = null
    private var dialog: Dialog? = null
    private var tvCategory: TextView? = null
    private var categoryResponse: CategoryResponse? = null
    private var postResponse: NewPostsearchDeailResponse? = null
    private var userResponse: UserSearchDetailResponse? = null
    private var city: String? = null
    private var area: String? = null
    private var categores: String? = null
    private var dataItem: DataItem? = null
    private var re_dataItem: ArrayList<DataItem>? = ArrayList();
    var chip: Chip? = null
    var chipGroup: ChipGroup? = null
    var category: String? = null
    var jj = 0
    var tab_position: Int? = null
    var Search = ""
    var retain_city: String? = null
    var retain_area: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_details
    }

    override fun getViewModel(): BaseViewModel<SearchDetailsNavigator> {
        searchDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchDetailsViewModel::class.java)
        return searchDetailsViewModel!!

    }

    override fun getViewBindingVarible(): Int {
        return BR.searchDetailsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSearchDetailsBinding = getViewDataBinding()

        val imgNotifiction =
            requireActivity().findViewById<View>(R.id.img_notification)

        val imgMenu =
            view.findViewById<View>(R.id.img_back)

        Search = "mak"

        editTextTextPersonName15.requestFocus()
        editTextTextPersonName15.addTextChangedListener(this)
        tab_position = tb_search.selectedTabPosition

        tb_search.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab_position = tab.position
                if (tab_position == 1) {
                    if (userResponse?.data != null) {
                        fragmentSearchDetailsBinding?.rvProfile?.visibility = View.VISIBLE
                        fragmentSearchDetailsBinding?.rvGallery?.visibility = View.INVISIBLE
                        var dataes = ArrayList<DataItems>()
                        for (i in userResponse?.data!!.indices) {
                            if (!appPreference.USERID.equals(userResponse?.data!!.get(i).id?.trim()))
                                dataes.add(userResponse?.data!!.get(i))
                        }
                        if (userResponse?.data != null) {
                            profileSearchAdapter = ProfileSearchAdapter(
                                dataes,
                                requireContext()
                            )
                            fragmentSearchDetailsBinding?.rvProfile?.adapter =
                                profileSearchAdapter
                        } else {

                        }
                    }
                } else {
                    fragmentSearchDetailsBinding?.rvProfile?.visibility = View.INVISIBLE
                    fragmentSearchDetailsBinding?.rvGallery?.visibility = View.VISIBLE
                    if (postResponse?.data != null) {

                        var datas = ArrayList<DataItemes>()

                        for (i in postResponse?.data!!.indices) {
                            if (!appPreference.USERID.equals(postResponse?.data!!.get(i).user_id?.trim()))
                                datas.add(postResponse?.data!!.get(i))
                        }
                        galleryAdapter =
                            VideoAdapter(datas, requireContext(), this@SearchDetailsFragment)
                        fragmentSearchDetailsBinding?.rvGallery?.adapter = galleryAdapter

                    } else {

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        imgMenu.setOnClickListener {
            requireActivity().onBackPressed()
        }

        imgNotifiction.setOnClickListener {
            filter()
        }

        getSearchDetails()

        fragmentSearchDetailsBinding?.rvGallery?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    fragmentSearchDetailsBinding?.constraintLayout7?.visibility = View.GONE
                    fragmentSearchDetailsBinding?.imgBack?.visibility = View.INVISIBLE
                    fragmentSearchDetailsBinding?.editTextTextPersonName15?.visibility = View.GONE
                    fragmentSearchDetailsBinding?.textView102?.visibility = View.GONE
                    fragmentSearchDetailsBinding?.tbSearch?.visibility = View.GONE

                    val layoutParams = (fragmentSearchDetailsBinding?.rvGallery?.layoutParams as? MarginLayoutParams)
                    layoutParams?.setMargins(0, 10, 0, 0)
                    fragmentSearchDetailsBinding?.rvGallery?.layoutParams = layoutParams

//                    val layoutParams = fragmentSearchDetailsBinding?.rvGallery as RecyclerView.LayoutParams
//                    layoutParams.setMargins(0, 10, 0, 0)
//                    fragmentSearchDetailsBinding?.rvGallery?.layoutParams = layoutParams
//                    fragmentSearchDetailsBinding?.rvGallery?.requestLayout()
//                    imgNotifiction.visibility = View.GONE

                } else if (dy < 0) {
                    println("Scrolled Upwards")
                    fragmentSearchDetailsBinding?.constraintLayout7?.visibility = View.VISIBLE
                    fragmentSearchDetailsBinding?.imgBack?.visibility = View.VISIBLE
                    fragmentSearchDetailsBinding?.editTextTextPersonName15?.visibility = View.VISIBLE
                    fragmentSearchDetailsBinding?.textView102?.visibility = View.VISIBLE
                    fragmentSearchDetailsBinding?.tbSearch?.visibility = View.VISIBLE
                    imgNotifiction.visibility = View.VISIBLE

                    val layoutParams = (fragmentSearchDetailsBinding?.rvGallery?.layoutParams as? MarginLayoutParams)
                    layoutParams?.setMargins(0, 660, 0, 0)
                    fragmentSearchDetailsBinding?.rvGallery?.layoutParams = layoutParams

//                    val layoutParams = fragmentSearchDetailsBinding?.rvGallery as RecyclerView.LayoutParams
//                    layoutParams.setMargins(0, 230, 0, 0)
//                    fragmentSearchDetailsBinding?.rvGallery?.layoutParams = layoutParams
//                    fragmentSearchDetailsBinding?.rvGallery?.requestLayout()

                } else {
                    println("No Vertical Scrolled")
                }
            }
        })

    }

    private fun filter() {
        try {
            var dialog = Dialog(requireActivity(), R.style.dialog_center)
            dialog.setContentView(R.layout.dialog_filter)
            tvCategory = dialog.findViewById<EditText>(R.id.edt_category)
            chip = Chip(requireContext())
            var tvSubmit = dialog.findViewById<TextView>(R.id.tv_submit)
            var tvLocation = dialog.findViewById<EditText>(R.id.edt_location)
            var Location = dialog.findViewById<EditText>(R.id.Locality)
            chipGroup = dialog.findViewById(R.id.chipGroup_filter)

            tvLocation.setText(retain_area)
            Location.setText(retain_city)

            dialog.show()

            var uri: ArrayList<String>? = ArrayList()
            if (re_dataItem != null) {
                for (i in re_dataItem!!.indices) {
                    if (re_dataItem!!.get(i).name!!.contains(",")) {
                        val arr: List<String> = categores!!.split(",")
                        val chip = Chip(requireContext())
                        chip.text = re_dataItem!!.get(i).name!!
                        chip.setTag(re_dataItem!!.get(i).id)
                        chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if ((i % 4) == 0) R.color.mate_red else if ((i % 3) == 0) R.color.mate_pink else R.color.txt_pink_chip)
                        chip.isCloseIconVisible = true
                        chip.setTextColor(Color.WHITE)
                        chip.chipCornerRadius = 10.0f
                        chip.setOnCloseIconClickListener(this)
                        chipGroup!!.addView(chip)
                    }
                }
            }

            tvLocation?.setText(area)
            Location?.setText(city)

            tvCategory?.setOnClickListener {

                val rvCategory =
                    dialog.findViewById<RecyclerView>(R.id.rv_category_filter)
                if (jj == 0) {

                    jj = 1
                    if (categoryResponse != null) {

                        rvCategory?.layoutManager = LinearLayoutManager(requireActivity())

                        val categoryAdapter = CategoryAdapter(
                            requireActivity(),
                            categoryResponse?.data as List<DataItem>,
                            this
                        )
                        rvCategory?.adapter = categoryAdapter
                        rvCategory.visibility = View.VISIBLE

                    } else {
                        category()
                    }
                } else if (jj == 1) {
                    jj = 0
                    rvCategory.visibility = View.GONE
                }
            }

            tvSubmit.setOnClickListener {

                area = tvLocation.text.toString()
                city = Location.text.toString()

                retain_area = area;
                retain_city = city;
                categores = category
                if (categores.isNullOrEmpty()) {
                    showToast("please select categories")
                    return@setOnClickListener
                }

                dialog.dismiss()

                searchDetailsViewModel!!.getSearchFilterPostDetails(
                    Location.text.toString(),
                    tvLocation.text.toString(),
                    category!!.trim()
                )
                    .observe(requireActivity(), Observer { response ->
                        if (response?.data != null) {
                            val getShopAlbumDetailsResponse =
                                response.data as NewPostsearchDeailResponse
                            postResponse = getShopAlbumDetailsResponse

                            if (tab_position == 0) {
                                fragmentSearchDetailsBinding?.rvProfile?.visibility =
                                    View.INVISIBLE
                                fragmentSearchDetailsBinding?.rvGallery?.visibility =
                                    View.VISIBLE

                                var datas = ArrayList<DataItemes>()

                                for (i in postResponse?.data!!.indices) {
                                    if (!appPreference.USERID.equals(postResponse?.data!!.get(i).user_id?.trim()))
                                        datas.add(postResponse?.data!!.get(i))
                                }
                                galleryAdapter =
                                    VideoAdapter(datas, requireContext(), this)
                                fragmentSearchDetailsBinding?.rvGallery?.adapter =
                                    galleryAdapter
                                category = ""
                            }
                            dialog.dismiss()

                        } else {
                            showToast(response.throwable?.message!!)
                        }
                    })

                searchDetailsViewModel!!.getSearchFilterUserDetails(
                    Location.text.toString(),
                    tvLocation.text.toString(),
                    category!!.trim()
                )
                    .observe(requireActivity(), Observer { response ->
                        if (response?.data != null) {
                            val getShopAlbumDetailsResponse =
                                response.data as UserSearchDetailResponse
                            userResponse = getShopAlbumDetailsResponse
                            if (tab_position == 1) {
                                fragmentSearchDetailsBinding?.rvProfile?.visibility =
                                    View.VISIBLE
                                fragmentSearchDetailsBinding?.rvGallery?.visibility =
                                    View.INVISIBLE

                                var dataes = ArrayList<DataItems>()

                                for (i in userResponse?.data!!.indices) {
                                    if (!appPreference.USERID.equals(userResponse?.data!!.get(i).id?.trim()))
                                        dataes.add(userResponse?.data!!.get(i))
                                }
                                profileSearchAdapter = ProfileSearchAdapter(
                                    dataes,
                                    requireContext()
                                )

                                category = ""

                                fragmentSearchDetailsBinding?.rvProfile?.adapter =
                                    profileSearchAdapter
                                dialog.dismiss()
                            }
                        }
                    })
            }
        } catch (e: Exception) {

        }
    }


    private fun setCategroy() {
        val rvCategory =
            dialog?.findViewById<RecyclerView>(R.id.rv_category_filter)
        rvCategory?.layoutManager = LinearLayoutManager(requireActivity())

        val categoryAdapter = CategoryAdapter(
            requireActivity(),
            categoryResponse?.data as List<DataItem>,
            this
        )
        rvCategory?.adapter = categoryAdapter
        rvCategory?.visibility = View.VISIBLE

    }

    private fun getSearchDetails() {
        searchDetailsViewModel!!.getSearchDetails(
            "mak"
        )
            .observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val getShopAlbumDetailsResponse = response.data as NewPostsearchDeailResponse
                    if (getShopAlbumDetailsResponse != null && getShopAlbumDetailsResponse?.data != null) {
                        postResponse = getShopAlbumDetailsResponse
                        if (tab_position != 1) {
                            fragmentSearchDetailsBinding?.rvProfile?.visibility = View.INVISIBLE
                            fragmentSearchDetailsBinding?.rvGallery?.visibility = View.VISIBLE
                            if (postResponse?.data != null) {
                                var datas = ArrayList<DataItemes>()

                                for (i in postResponse?.data!!.indices) {
                                    if (!appPreference.USERID.equals(postResponse?.data!!.get(i).user_id?.trim()))
                                        datas.add(postResponse?.data!!.get(i))
                                }
                                galleryAdapter =
                                    VideoAdapter(datas, requireContext(), this)
                                fragmentSearchDetailsBinding?.rvGallery?.adapter =
                                    galleryAdapter
                            }
                        }
                    } else {
//                        showToast("No results found")
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    private fun category() {
        showLoading()
        searchDetailsViewModel!!.getCategory().observe(requireActivity(), Observer { response ->
            hideLoading()

            if (response?.data != null) {
                val categoryResponse = response.data as CategoryResponse
                if (categoryResponse != null) {
                    if (categoryResponse.equals("")) {
                        showToast(getString(R.string.no_category_found))
                    } else {
                        this.categoryResponse = categoryResponse
                        setCategroy()
                    }
                } else {
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onItemClick(dataItemList: DataItem, position: Int) {
        this.dataItem = dataItemList
        re_dataItem?.add(dataItemList)
        if (!chipGroup!!.childCount.equals(null)) {
            for (i in 0 until chipGroup!!.childCount) {
                val chip = chipGroup!!.getChildAt(i) as Chip
                if (chip.text.equals(dataItemList.name)) {
                    showToast("Already added")
                    return
                }
            }
        }

        val chip = Chip(requireContext())
        chip.text = dataItemList.name.toString()
        chip.setTag(dataItemList.id)
        chip.setChipBackgroundColorResource(if ((position % 5) == 0) R.color.txt_orange else if ((position % 4) == 0) R.color.mate_red else if ((position % 3) == 0) R.color.mate_pink else R.color.txt_pink_chip)
        chip.isCloseIconVisible = true
        chip.setTextColor(Color.WHITE)
        chip.chipCornerRadius = 10.0f
        chip.setOnCloseIconClickListener(this)
        chipGroup!!.addView(chip)
        if (dataItemList?.name!!.equals("Tattoo & Piercing")) {
            if (!category.isNullOrEmpty()) {
                category = categores + "Tattoo &amp; Piercing" + " "
            } else {
                category = "Tattoo &amp; Piercing" + " "
            }
        } else if (dataItemList?.name!!.equals("Spa & Massage")) {
            if (!category.isNullOrEmpty()) {
                category = categores + "Spa &amp; Massage" + " "
            } else {
                category = "Spa &amp; Massage" + " "
            }
        } else if (dataItemList?.name!!.equals("Waxing & Hair Removal")) {
            if (!category.isNullOrEmpty()) {
                category = categores + "Waxing &amp; Hair Removal" + " "
            } else {
                category = "Waxing &amp; Hair Removal" + " "
            }
        } else if (dataItemList?.name!!.equals("Nail Art & Care")) {
            if (!category.isNullOrEmpty()) {
                category = categores + "Waxing &amp; Hair Removal" + " "
            } else {
                category = "Waxing &amp; Hair Removal" + " "
            }
        } else {
            if (!categores.isNullOrEmpty()) {
                category = category + dataItem?.name.toString() + " "
            } else {
                category = dataItem?.name.toString() + " "
            }
        }

    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        //Post Search
        searchDetailsViewModel!!.getSearchDetails(
            s.toString()
        )
            .observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    editTextTextPersonName15.requestFocus()

                    val getShopAlbumDetailsResponse =
                        response.data as NewPostsearchDeailResponse

                    if (getShopAlbumDetailsResponse.data != null) {
                        postResponse = getShopAlbumDetailsResponse
                        if (tab_position == 1) {

                            fragmentSearchDetailsBinding?.rvProfile?.visibility =
                                View.VISIBLE
                            fragmentSearchDetailsBinding?.rvGallery?.visibility =
                                View.INVISIBLE

                            var dataes = ArrayList<DataItems>()

                            for (i in userResponse?.data!!.indices) {
                                if (!appPreference.USERID.equals(userResponse?.data!!.get(i).id?.trim()))
                                    dataes.add(userResponse?.data!!.get(i))
                            }
                            profileSearchAdapter = ProfileSearchAdapter(
                                dataes,
                                requireContext()
                            )

                            fragmentSearchDetailsBinding?.rvProfile?.adapter =
                                profileSearchAdapter

                        } else {
                            if (!postResponse?.data.isNullOrEmpty()) {

                                fragmentSearchDetailsBinding?.rvProfile?.visibility =
                                    View.INVISIBLE
                                fragmentSearchDetailsBinding?.rvGallery?.visibility =
                                    View.VISIBLE
                                var datas = ArrayList<DataItemes>()

                                for (i in postResponse?.data!!.indices) {
                                    if (!appPreference.USERID.equals(postResponse?.data!!.get(i).user_id?.trim())) {
                                        datas.add(postResponse?.data!!.get(i))
                                        if (!editTextTextPersonName15.text.toString()
                                                .equals("")
                                        ) {
                                            Search =
                                                editTextTextPersonName15.text.toString().trim()
                                        }
                                    }
                                }

                                galleryAdapter =
                                    VideoAdapter(datas, requireContext(), this)
                                fragmentSearchDetailsBinding?.rvGallery?.adapter =
                                    galleryAdapter
                                fragmentSearchDetailsBinding?.rvGallery?.setDescendantFocusability(
                                    RecyclerView.FOCUS_AFTER_DESCENDANTS
                                );

                                editTextTextPersonName15.requestFocus()
                                showkeyboard()

                                Handler(Looper.getMainLooper()).postDelayed({
                                }, 0)

                            }
                        }
                    } else {
                        if (tab_position != 1) {
//                            showToast("No results found")
                        }
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }

            })

        //user Search
        searchDetailsViewModel!!.getSearchUserDetails(
            "",
            s.toString(),
            "",
            ""
        )
            .observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    editTextTextPersonName15.requestFocus()

                    val getShopAlbumDetailsResponse =
                        response.data as UserSearchDetailResponse
                    if (getShopAlbumDetailsResponse.data != null) {
                        userResponse = getShopAlbumDetailsResponse
                        if (tab_position != 0) {

                            editTextTextPersonName15.requestFocus()
                            fragmentSearchDetailsBinding?.rvProfile?.visibility = View.VISIBLE
                            fragmentSearchDetailsBinding?.rvGallery?.visibility = View.INVISIBLE

                            var dataes = ArrayList<DataItems>()

                            for (i in userResponse?.data!!.indices) {
                                if (!appPreference.USERID.equals(userResponse?.data!!.get(i).id?.trim()))
                                    dataes.add(userResponse?.data!!.get(i))
                            }

                            profileSearchAdapter = ProfileSearchAdapter(
                                dataes,
                                requireContext()
                            )

                            fragmentSearchDetailsBinding?.rvProfile?.adapter =
                                profileSearchAdapter

                        } else {
                            if (!postResponse?.data.isNullOrEmpty()) {

                                editTextTextPersonName15.requestFocus()
                                fragmentSearchDetailsBinding?.rvProfile?.visibility =
                                    View.INVISIBLE
                                fragmentSearchDetailsBinding?.rvGallery?.visibility =
                                    View.VISIBLE
                                var datas = ArrayList<DataItemes>()

                                for (i in postResponse?.data!!.indices) {
                                    if (!appPreference.USERID.equals(postResponse?.data!!.get(i).user_id?.trim())) {
                                        datas.add(postResponse?.data!!.get(i))
                                        if (!editTextTextPersonName15.text.toString()
                                                .equals("")
                                        ) {
                                            Search =
                                                editTextTextPersonName15.text.toString().trim()

                                            editTextTextPersonName15.requestFocus()

                                        }
                                    }
                                }

                                galleryAdapter =
                                    VideoAdapter(datas, requireContext(), this)
                                fragmentSearchDetailsBinding?.rvGallery?.adapter =
                                    galleryAdapter
                            }
                        }
                    } else {
                        if (tab_position == 1) {
//                            showToast("No results found")
                        }
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })

    }

    private fun showkeyboard() {
        editTextTextPersonName15.requestFocus()
        val imgr =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(editTextTextPersonName15, InputMethodManager.SHOW_IMPLICIT)
    }


    override fun onClick(v: View?) {
        chipGroup?.removeView(v)
    }

    override fun onItemLickClicksOfProfile(
        videoItem: List<DataItemes>?,
        position: Int
    ) {

        startActivity(
            Intent(requireContext(), NotificationActivity::class.java)
                .putExtra("PostFilterAdapter", "PostFilterAdapter")
                .putExtra("position", position)
                .putExtra("area", area)
                .putExtra("city", city)
                .putExtra("search", Search)
                .putExtra("category", categores)
                .putExtra("private_list", Gson().toJson(videoItem))
        )
    }

    override fun enter() {
        editTextTextPersonName15.requestFocus()
        showkeyboard()
    }
}