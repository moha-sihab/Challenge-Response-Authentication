package com.mohasihab.cram.core.data.remote.request

data class ChallengeResponseRequest(
    val challengeId: Int,
    val signature: String,
    val userId: Int
)