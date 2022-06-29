package com.mdq.social.app.data.di.modules

import com.mdq.social.ui.addadmin.AddAdminActivity
import com.mdq.social.ui.addpost.AddpostActivity
import com.mdq.social.ui.blockaccount.BlockAccountActivity
import com.mdq.social.ui.blockcomment.BlockCommentActivity
import com.mdq.social.ui.bookmark.BookMarkActivity
import com.mdq.social.ui.businessupdate.BusinessUpdateActivity
import com.mdq.social.ui.follow.FollowActivity
import com.mdq.social.ui.forgot.ForgotPasswordActivity
import com.mdq.social.ui.freelanceupdate.FreelanceUpdateActivity
import com.mdq.social.ui.help.HelpActivity
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.individual.IndividualActivity
import com.mdq.social.ui.livechat.LiveChatActivity
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.ui.notification.NotificationActivity
import com.mdq.social.ui.notificationsetting.NotificationSettingActivity
import com.mdq.social.ui.otp.OtpActivity
import com.mdq.social.ui.pendingrequest.PendingRequestActivity
import com.mdq.social.ui.post.PostActivity
import com.mdq.social.ui.privacy.PrivacyActivity
import com.mdq.social.ui.resetpassword.ResetPasswordActivity
import com.mdq.social.ui.reviewlist.ReviewListActivity
import com.mdq.social.ui.selectedpost.SelectedPostActivity
import com.mdq.social.ui.setting.SettingActivity
import com.mdq.social.ui.signup.SignUpActivity
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import com.mdq.social.ui.signupfreelancer.SignUpFreelancerActivity
import com.mdq.social.ui.signupselection.SignUpSelectionActivity
import com.mdq.social.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector
    fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    fun contributeSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    fun contributeAddAdminActivity(): AddAdminActivity

    @ContributesAndroidInjector
    fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    fun contributeSettingActivity(): SettingActivity

    @ContributesAndroidInjector
    fun contributeSignupSelectionActivity(): SignUpSelectionActivity

    @ContributesAndroidInjector
    fun contributeSignupFreelancerActivity(): SignUpFreelancerActivity

    @ContributesAndroidInjector
    fun contributeSignupBusinessActivity(): SignUpBusinessActivity

    @ContributesAndroidInjector
    fun contributePostActivity(): PostActivity

    @ContributesAndroidInjector
    fun contributeAddPostActivity(): AddpostActivity

    @ContributesAndroidInjector
    fun contributeSelectedPostActivity(): SelectedPostActivity

    @ContributesAndroidInjector
    fun contributeLiveChatActivity(): LiveChatActivity

    @ContributesAndroidInjector
    fun contributeNotificationActivity(): NotificationActivity

    @ContributesAndroidInjector
    fun contributeResetPasswordActivity(): ResetPasswordActivity

    @ContributesAndroidInjector
    fun contributeOtpActivity(): OtpActivity

    @ContributesAndroidInjector
    fun contributeForgotPasswordActivity(): ForgotPasswordActivity

    @ContributesAndroidInjector
    fun contributeBookMarkActivity(): BookMarkActivity

    @ContributesAndroidInjector
    fun contributePendingRequestActivity(): PendingRequestActivity

    @ContributesAndroidInjector
    fun contributeFollowActivity(): FollowActivity

    @ContributesAndroidInjector
    fun contributeBlockAccountActivity(): BlockAccountActivity

    @ContributesAndroidInjector
    fun contributeNotificationSettingActivity(): NotificationSettingActivity

    @ContributesAndroidInjector
    fun contributeHelpActivity(): HelpActivity

    @ContributesAndroidInjector
    fun contributePrivacyActivity(): PrivacyActivity

    @ContributesAndroidInjector
    fun contributeBlockCommentActivity(): BlockCommentActivity

    @ContributesAndroidInjector
    fun contributeIndividualActivity(): IndividualActivity

    @ContributesAndroidInjector
    fun contributeFreelanceUpdateActivity(): FreelanceUpdateActivity

    @ContributesAndroidInjector
    fun contributeBusinessUpdateActivity(): BusinessUpdateActivity

    @ContributesAndroidInjector
    fun contributeReviewListActivity(): ReviewListActivity


}