package com.mohasihab.cram.core.di

import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.interfaces.UserRepositoryContract
import com.mohasihab.cram.core.data.repositories.LoginRepository
import com.mohasihab.cram.core.data.repositories.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepositoryContract> { LoginRepository(get()) }
    single<UserRepositoryContract> { UserRepository(get()) }
}