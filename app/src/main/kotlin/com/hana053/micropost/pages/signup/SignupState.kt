package com.hana053.micropost.pages.signup

import com.hana053.micropost.interactor.UserInteractor


data class SignupState(
    var fullName: String = "",
    var email: String = "",
    var password: String = ""
) {

    fun toSignupRequest() = UserInteractor.SignupRequest(fullName, email, password)

}