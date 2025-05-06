package com.mohasihab.cram.core.data.remote

import com.mohasihab.cram.core.data.remote.request.ChallengeResponseRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.ChallengeResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ChallengeResponseApi {
    @POST("/api/Challenge")
    suspend fun createChallenge(
        @Query("userId") userId : Int
    ): BaseResponse<ChallengeResponse>

    @POST("/api/Challenge/response")
    suspend fun responseChallenge(
        @Body challengeResponseRequest: ChallengeResponseRequest
    ) : BaseResponse<LoginResponse>
}