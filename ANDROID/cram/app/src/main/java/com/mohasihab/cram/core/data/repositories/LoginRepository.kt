package com.mohasihab.cram.core.data.repositories

import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.remote.LoginApi
import com.mohasihab.cram.core.data.remote.request.LoginRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse
import com.mohasihab.cram.core.helper.NetworkHelper.safeApiCall
import retrofit2.HttpException
import java.io.IOException

class LoginRepository (
    private val loginApi: LoginApi
) : LoginRepositoryContract {
    override suspend fun login(loginRequest: LoginRequest): BaseResponse<LoginResponse> {
        return safeApiCall {
            loginApi.login(loginRequest)
        }
    }
}