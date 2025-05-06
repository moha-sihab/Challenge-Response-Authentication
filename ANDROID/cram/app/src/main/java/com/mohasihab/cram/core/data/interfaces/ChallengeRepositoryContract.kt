package com.mohasihab.cram.core.data.interfaces

import com.mohasihab.cram.core.data.remote.request.ChallengeResponseRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.ChallengeResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse


interface ChallengeRepositoryContract {
    suspend fun createChallenge(
        userId : Int
    ): BaseResponse<ChallengeResponse>

    suspend fun responseChallenge(
       challengeResponseRequest: ChallengeResponseRequest
    ) : BaseResponse<LoginResponse>
}