package com.mohasihab.cram.core.data.remote

import com.mohasihab.cram.core.data.remote.request.LoginRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/Users/login")
    suspend fun login(
       @Body loginRequest : LoginRequest
    ): BaseResponse<LoginResponse>
}