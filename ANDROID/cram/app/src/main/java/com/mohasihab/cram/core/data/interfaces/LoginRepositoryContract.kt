package com.mohasihab.cram.core.data.interfaces

import com.mohasihab.cram.core.data.remote.request.LoginRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse

interface LoginRepositoryContract {
    suspend fun login(loginRequest: LoginRequest) : BaseResponse<LoginResponse>
}