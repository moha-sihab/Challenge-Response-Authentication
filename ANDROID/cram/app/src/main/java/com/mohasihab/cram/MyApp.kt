package com.mohasihab.cram

import android.app.Application
import com.mohasihab.cram.core.di.coreModule
import com.mohasihab.cram.core.di.networkModule
import com.mohasihab.cram.core.di.repositoryModule
import com.mohasihab.cram.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(
                networkModule,
                coreModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}