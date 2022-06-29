package com.mdq.social.ui.signupfreelancer

import okhttp3.MultipartBody
import okhttp3.RequestBody

public class FreelancerRequestBody {
    var type: RequestBody? = null
    var user_name: RequestBody? = null
    var email: RequestBody? = null
    var address: RequestBody? = null
    var password: RequestBody? = null
    var dob: RequestBody? = null
    var mobile: RequestBody? = null
    var service: RequestBody? = null
    var categories: RequestBody? = null
    var certificate_proof: MultipartBody.Part? = null

}