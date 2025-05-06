package com.mohasihab.cram.core.di

import com.mohasihab.cram.BuildConfig
import com.mohasihab.cram.core.data.local.PreferenceManager
import com.mohasihab.cram.core.data.remote.ChallengeResponseApi
import com.mohasihab.cram.core.data.remote.LoginApi
import com.mohasihab.cram.core.data.remote.UserApi
import com.mohasihab.cram.core.helper.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { PreferenceManager(get()) }
    single { AuthInterceptor(get()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single<LoginApi> { get<Retrofit>().create(LoginApi::class.java) }
    single<UserApi> { get<Retrofit>().create(UserApi::class.java) }
    single<ChallengeResponseApi> { get<Retrofit>().create(ChallengeResponseApi::class.java) }
}

fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}