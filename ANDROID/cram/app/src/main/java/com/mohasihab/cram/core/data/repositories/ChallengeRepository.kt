package com.mohasihab.cram.core.data.repositories

import com.mohasihab.cram.core.data.interfaces.ChallengeRepositoryContract
import com.mohasihab.cram.core.data.remote.ChallengeResponseApi
import com.mohasihab.cram.core.data.remote.request.ChallengeResponseRequest

class ChallengeRepository(
    private val challengeResponseApi: ChallengeResponseApi
) : ChallengeRepositoryContract {
    override suspend fun createChallenge(userId: Int) = challengeResponseApi.createChallenge(userId)

    override suspend fun responseChallenge(challengeResponseRequest: ChallengeResponseRequest) = challengeResponseApi.responseChallenge(challengeResponseRequest)
}