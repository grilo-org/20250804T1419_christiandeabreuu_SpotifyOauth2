package com.example.spotifyapi.oauth2.di

import com.example.spotifyapi.oauth2.domain.interfacies.AuthRepository
import com.example.spotifyapi.oauth2.data.repository.AuthRepositoryImpl
import com.example.spotifyapi.oauth2.domain.usecase.AuthUseCase
import com.example.spotifyapi.oauth2.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.oauth2.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val authModules = module {
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { GetAccessTokenUseCase(get()) }
    factory { AuthUseCase(get(), get()) }
    viewModel { LoginViewModel(get()) }
}


