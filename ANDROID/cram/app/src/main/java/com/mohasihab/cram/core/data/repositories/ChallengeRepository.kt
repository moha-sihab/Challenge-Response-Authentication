package com.mohasihab.cram.core.data.repositories

import com.mohasihab.cram.core.data.interfaces.ChallengeRepositoryContract
import com.mohasihab.cram.core.data.remote.ChallengeResponseApi
import com.mohasihab.cram.core.data.remote.request.ChallengeResponseRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.ChallengeResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse
import com.mohasihab.cram.core.helper.NetworkHelper.safeApiCall

class ChallengeRepository(
    private val challengeResponseApi: ChallengeResponseApi
) : ChallengeRepositoryContract {
    override suspend fun createChallenge(userId: Int): BaseResponse<ChallengeResponse> {
        return safeApiCall {
            challengeResponseApi.createChallenge(userId)
        }
    }

    override suspend fun responseChallenge(challengeResponseRequest: ChallengeResponseRequest): BaseResponse<LoginResponse> {
        return safeApiCall {
            challengeResponseApi.responseChallenge(challengeResponseRequest)
        }
    }
}