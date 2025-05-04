package com.mohasihab.cram.core.data.remote.request

data class ChallengeRespondRequest(
    val challengeId: Int,
    val signature: String,
    val userId: Int
)