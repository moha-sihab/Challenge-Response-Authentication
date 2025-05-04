package com.mohasihab.cram.core.di

import com.mohasihab.cram.core.data.local.TokenManager
import org.koin.dsl.module

val coreModule = module {
    single { TokenManager(get()) }
}