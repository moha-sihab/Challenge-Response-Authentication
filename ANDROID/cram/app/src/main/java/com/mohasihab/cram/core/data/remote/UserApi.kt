package com.mohasihab.cram.core.data.remote

import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.UpdatePublicKeyResponse
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @PUT("/api/Users/{id}/public-key")
    suspend fun sendPublicKey(
        @Path("id") userId: Int,
        @Body publicKey: PublicKeyRequest
    ): BaseResponse<UpdatePublicKeyResponse>
}