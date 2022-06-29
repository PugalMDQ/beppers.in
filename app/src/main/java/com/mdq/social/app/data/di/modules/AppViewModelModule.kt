package com.mdq.social.app.data.di.modules


import androidx.lifecycle.ViewModel
import com.mdq.social.app.data.di.keys.ViewModelKey
import com.mdq.social.app.data.viewmodels.reviewlist.ReviewListViewModel
import com.mdq.social.app.data.viewmodels.pendingrequest.PendingRequestViewModel
import com.mdq.social.app.data.viewmodels.add.AddViewModel
import com.mdq.social.app.data.viewmodels.addadmin.AddAdminViewModel
import com.mdq.social.app.data.viewmodels.addpost.AddPostViewModel
import com.mdq.social.app.data.viewmodels.blockaccount.BlockAccountViewModel
import com.mdq.social.app.data.viewmodels.blockcomment.BlockCommentViewModel
import com.mdq.social.app.data.viewmodels.bookmark.BookMarkViewModel
import com.mdq.social.app.data.viewmodels.business.BusinessViewModel
import com.mdq.social.app.data.viewmodels.businessupdate.BusinessUpdateViewModel
import com.mdq.social.app.data.viewmodels.chat.ChatViewModel
import com.mdq.social.app.data.viewmodels.follow.FollowViewModel
import com.mdq.social.app.data.viewmodels.follower.FollowerViewModel
import com.mdq.social.app.data.viewmodels.following.FollowingViewModel
import com.mdq.social.app.data.viewmodels.forgot.ForgotPasswordViewModel
import com.mdq.social.app.data.viewmodels.freelancer.FreelancerViewModel
import com.mdq.social.app.data.viewmodels.freelanceupdate.FreelanceUpdateViewModel
import com.mdq.social.app.data.viewmodels.help.HelpViewModel
import com.mdq.social.app.data.viewmodels.home.HomeViewModel
import com.mdq.social.app.data.viewmodels.individual.IndividualViewModel
import com.mdq.social.app.data.viewmodels.livechat.LiveChatViewModel
import com.mdq.social.app.data.viewmodels.login.LoginViewModel
import com.mdq.social.app.data.viewmodels.notification.NotificationViewModel
import com.mdq.social.app.data.viewmodels.notificationsetting.NotificationSettingViewModel
import com.mdq.social.app.data.viewmodels.post.PostViewModel
import com.mdq.social.app.data.viewmodels.privacy.PrivacyViewModel
import com.mdq.social.app.data.viewmodels.profile.ProfileViewModel
import com.mdq.social.app.data.viewmodels.rate.RateUsViewModel
import com.mdq.social.app.data.viewmodels.search.SearchViewModel
import com.mdq.social.app.data.viewmodels.searchdetails.SearchDetailsViewModel
import com.mdq.social.app.data.viewmodels.selectpost.SelectPostViewModel
import com.mdq.social.app.data.viewmodels.setting.SettingViewModel
import com.mdq.social.app.data.viewmodels.signup.SignupViewModel
import com.mdq.social.app.data.viewmodels.signupbusiness.SignupBusinessModel
import com.mdq.social.app.data.viewmodels.signupfreelancer.SignupFreelancerModel
import com.mdq.social.app.data.viewmodels.signupselection.SignupSelectionModel
import com.mdq.social.app.data.viewmodels.splash.SplashViewModel
import com.mdq.social.app.data.viewmodels.terms.TermsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel::class)
    internal abstract fun bindSignupViewModel(signupViewModel: SignupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddAdminViewModel::class)
    internal abstract fun bindAddAdminViewModel(addAdminViewModel: AddAdminViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddViewModel::class)
    internal abstract fun bindAddViewModel(addViewModel: AddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    internal abstract fun bindSettingViewModel(settingViewModel: SettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupSelectionModel::class)
    internal abstract fun bindSignUpSelectionViewModel(signupSelectionModel: SignupSelectionModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupFreelancerModel::class)
    internal abstract fun bindSignUpFreelancerViewModel(signupFreelanceerModel: SignupFreelancerModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignupBusinessModel::class)
    internal abstract fun bindSignUpBusinessViewModel(signupBusinessModel: SignupBusinessModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchDetailsViewModel::class)
    internal abstract fun bindSearchDetailsViewModel(searchDetailsViewModel: SearchDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindChatViewModel(chatViewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    internal abstract fun bindPostViewModel(postViewModel: PostViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(BusinessViewModel::class)
    internal abstract fun bindBusinessViewModel(businessViewModel: BusinessViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FreelancerViewModel::class)
    internal abstract fun bindFreelancerViewModel(freelancerViewModel: FreelancerViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(AddPostViewModel::class)
    internal abstract fun bindAddPostViewModel(addPostViewModel: AddPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectPostViewModel::class)
    internal abstract fun bindSelectViewModel(selectPostViewModel: SelectPostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RateUsViewModel::class)
    internal abstract fun bindRateUsViewModel(rateUsViewModel: RateUsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    internal abstract fun bindNotificationViewModel(notificationViewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LiveChatViewModel::class)
    internal abstract fun bindLiveChatViewModel(liveChatViewModel: LiveChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TermsViewModel::class)
    internal abstract fun bindTermsViewModel(termsViewModel: TermsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookMarkViewModel::class)
    internal abstract fun bindBookMarkViewModel(bookMarkViewModel: BookMarkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    internal abstract fun bindForgotPasswordViewModel(forgotPasswordViewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PendingRequestViewModel::class)
    internal abstract fun bindPendingRequestViewModel(pendingRequestViewModel: PendingRequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowerViewModel::class)
    internal abstract fun bindFollowerViewModel(followerViewModel: FollowerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowingViewModel::class)
    internal abstract fun bindFollowingViewModel(followingViewModel: FollowingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowViewModel::class)
    internal abstract fun bindFollowViewModel(followViewModel: FollowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlockAccountViewModel::class)
    internal abstract fun bindBlockAccountViewModel(blockAccountViewModel: BlockAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotificationSettingViewModel::class)
    internal abstract fun bindNotificationSettingViewModel(notificationSettingViewModel: NotificationSettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HelpViewModel::class)
    internal abstract fun bindHelpViewModel(helpViewModel: HelpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyViewModel::class)
    internal abstract fun bindPrivacyViewModel(privacyViewModel: PrivacyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlockCommentViewModel::class)
    internal abstract fun bindBlockCommentViewModel(blockCommentViewModel: BlockCommentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IndividualViewModel::class)
    internal abstract fun bindIndividualViewModel(individualViewModel: IndividualViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BusinessUpdateViewModel::class)
    internal abstract fun bindBusinessUpdateViewModel(businessUpdateViewModel: BusinessUpdateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FreelanceUpdateViewModel::class)
    internal abstract fun bindFreelanceUpdateViewModel(freelanceUpdateViewModel: FreelanceUpdateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReviewListViewModel::class)
    internal abstract fun bindReviewListViewModel(reviewListViewModel: ReviewListViewModel): ViewModel
}