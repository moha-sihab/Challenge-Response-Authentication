package com.mohasihab.cram.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdatePublicKeyResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("publicKey")
    val publicKey: String? = null,
)
