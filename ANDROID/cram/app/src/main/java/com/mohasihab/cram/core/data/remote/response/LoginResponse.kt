package com.mohasihab.cram.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("token")
    val token: String?
)
