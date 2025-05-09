package com.mohasihab.cram.core.helper

import com.mohasihab.cram.BuildConfig
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkHelper {
    fun retrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient())
            .build()
    }

    private fun okHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideCacheInterceptor())
            .callTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        return okhttp.build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun provideCacheInterceptor() = run {
        okhttp3.Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }
    suspend fun <T> safeApiCall(apiCall: suspend () -> BaseResponse<T>): BaseResponse<T> {
        return try {
            apiCall()
        } catch (e: HttpException) {
            val errorMsg = when (e.code()) {
                401 -> "Unauthorized. Invalid or expired credentials."
                403 -> "Forbidden. Access denied."
                else -> "Server error: ${e.message()}"
            }
            BaseResponse.failed(errorMsg)
        } catch (e: IOException) {
            BaseResponse.failed("Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            BaseResponse.failed("Unexpected error: ${e.localizedMessage}")
        }
    }

}