package com.hana053.micropost.domain


interface Avatar {
    val avatarHash: String

    fun avatarUrl(size: Int = 72) = "https://secure.gravatar.com/avatar/$avatarHash?s=$size"
}
