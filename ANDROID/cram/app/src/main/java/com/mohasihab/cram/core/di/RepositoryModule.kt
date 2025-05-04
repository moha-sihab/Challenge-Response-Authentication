package com.mohasihab.cram.core.di

import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.repositories.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepositoryContract> { LoginRepository(get()) } // get() resolves LoginApi
}