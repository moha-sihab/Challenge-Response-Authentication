package com.mohasihab.cram.core.data.repositories

import com.mohasihab.cram.core.data.interfaces.UserRepositoryContract
import com.mohasihab.cram.core.data.remote.UserApi
import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.UpdatePublicKeyResponse

class UserRepository(
    private val userApi: UserApi
) : UserRepositoryContract {
    override suspend fun sendPublicKey(
        userId: Int,
        publicKey: PublicKeyRequest
    ): BaseResponse<UpdatePublicKeyResponse> = userApi.sendPublicKey(userId,publicKey)
}