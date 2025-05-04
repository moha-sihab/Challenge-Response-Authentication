package com.mohasihab.cram.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChallengeResponse(
    @SerializedName("challengeText")
    val challengeText: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("issuedAt")
    val issuedAt: String? = null
)
