package com.mohasihab.cram.core.data.repositories

import com.mohasihab.cram.core.data.interfaces.UserRepositoryContract
import com.mohasihab.cram.core.data.remote.UserApi
import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.UpdatePublicKeyResponse
import com.mohasihab.cram.core.helper.NetworkHelper.safeApiCall
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val userApi: UserApi
) : UserRepositoryContract {
    override suspend fun sendPublicKey(
        userId: Int,
        publicKey: PublicKeyRequest
    ): BaseResponse<UpdatePublicKeyResponse> {
        return safeApiCall {
            userApi.sendPublicKey(userId, publicKey)
        }
    }
}