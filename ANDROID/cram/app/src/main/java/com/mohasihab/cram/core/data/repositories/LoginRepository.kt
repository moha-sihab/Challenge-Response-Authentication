package com.mohasihab.cram.core.data.repositories

import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.remote.LoginApi
import com.mohasihab.cram.core.data.remote.request.LoginRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse

class LoginRepository (
    private val loginApi: LoginApi
) : LoginRepositoryContract {
    override suspend fun login(loginRequest: LoginRequest): BaseResponse<LoginResponse> = loginApi.login(loginRequest)
}