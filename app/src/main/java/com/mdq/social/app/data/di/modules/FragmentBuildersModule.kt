package com.mdq.social.app.data.di.modules


import com.mdq.social.app.data.di.keys.FragmentKey
import com.mdq.social.ui.add.AddFragment
import com.mdq.social.ui.business.BusinessProfileFragment
import com.mdq.social.ui.chat.ChatFragment
import com.mdq.social.ui.follow.FollowerFragment
import com.mdq.social.ui.follow.FollowingFragment
import com.mdq.social.ui.freelancer.FreelancerFragment
import com.mdq.social.ui.home.HomeFragment
import com.mdq.social.ui.profile.ProfileFragment
import com.mdq.social.ui.rate.RateUsFragment
import com.mdq.social.ui.search.SearchFragment
import com.mdq.social.ui.searchdetails.*
import com.mdq.social.ui.terms.TermsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface FragmentBuildersModule {

    @ContributesAndroidInjector()
    @FragmentKey(HomeFragment::class)
    fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector()
    @FragmentKey(AddFragment::class)
    fun contributeAddFragment(): AddFragment

    @ContributesAndroidInjector()
    @FragmentKey(ProfileFragment::class)
    fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector()
    @FragmentKey(SearchFragment::class)
    fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector()
    @FragmentKey(SearchDetailsFragment::class)
    fun contributeSearchDetailsFragment(): SearchDetailsFragment

    @ContributesAndroidInjector()
    @FragmentKey(PhotoFragment::class)
    fun contributePhotoFragment(): PhotoFragment

    @ContributesAndroidInjector()
    @FragmentKey(VideoFrgment::class)
    fun contributeVideoFrgment(): VideoFrgment

    @ContributesAndroidInjector()
    @FragmentKey(SearchProfileFragment::class)
    fun contributeSearchProfileFragment(): SearchProfileFragment

    @ContributesAndroidInjector()
    @FragmentKey(LocationFragment::class)
    fun contributeLocationFragment(): LocationFragment

    @ContributesAndroidInjector()
    @FragmentKey(AllFragment::class)
    fun contributeAllFragment(): AllFragment

    @ContributesAndroidInjector()
    @FragmentKey(ChatFragment::class)
    fun contributeChatFragment(): ChatFragment

    @ContributesAndroidInjector()
    @FragmentKey(BusinessProfileFragment::class)
    fun contributeBusinessProfileFragment(): BusinessProfileFragment

    @ContributesAndroidInjector()
    @FragmentKey(FreelancerFragment::class)
    fun contributeFreelancerFragment(): FreelancerFragment

    @ContributesAndroidInjector()
    @FragmentKey(FollowerFragment::class)
    fun contributeFollowerFragment(): FollowerFragment

    @ContributesAndroidInjector()
    @FragmentKey(RateUsFragment::class)
    fun contributeRateUsFragment(): RateUsFragment

    @ContributesAndroidInjector()
    @FragmentKey(TermsFragment::class)
    fun contributeTermsFragment(): TermsFragment

    @ContributesAndroidInjector()
    @FragmentKey(FollowingFragment::class)
    fun contributeFollowingFragment(): FollowingFragment


}