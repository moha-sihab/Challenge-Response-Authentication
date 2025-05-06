package com.mohasihab.cram.ui.di

import com.mohasihab.cram.ui.home.HomeViewModel
import com.mohasihab.cram.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(),get(), get()) }
    viewModel { HomeViewModel(get(),get()) }
}