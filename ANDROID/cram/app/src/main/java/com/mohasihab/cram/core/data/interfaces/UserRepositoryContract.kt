package com.mohasihab.cram.core.data.interfaces

import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.UpdatePublicKeyResponse

interface UserRepositoryContract {
    suspend fun sendPublicKey(
        userId: Int,
        publicKey: PublicKeyRequest
    ): BaseResponse<UpdatePublicKeyResponse>
}