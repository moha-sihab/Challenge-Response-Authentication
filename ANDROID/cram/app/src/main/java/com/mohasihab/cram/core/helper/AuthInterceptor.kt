package com.mohasihab.cram.core.helper

import android.util.Log
import com.mohasihab.cram.core.data.local.PreferenceManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val preferenceManager: PreferenceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token =  preferenceManager.getString(PreferenceKeys.Auth.ACCESS_TOKEN)

        Log.d("AuthInterceptor", "Token: interceptor : $token")
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        val request = requestBuilder.build()
        Log.d("AuthInterceptor", "Final headers:")
        request.headers.names().forEach {
            Log.d("AuthInterceptor", "$it = ${request.header(it)}")
        }
        return chain.proceed(requestBuilder.build())
    }
}