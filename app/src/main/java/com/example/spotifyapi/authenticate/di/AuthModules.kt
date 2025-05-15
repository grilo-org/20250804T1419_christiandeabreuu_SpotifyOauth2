package com.example.spotifyapi.authenticate.di

import com.example.spotifyapi.authenticate.domain.interfacies.AuthRepository
import com.example.spotifyapi.authenticate.data.repository.AuthRepositoryImpl
import com.example.spotifyapi.authenticate.domain.usecase.AuthUseCase
import com.example.spotifyapi.authenticate.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.authenticate.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.authenticate.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val authModules = module {
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { GetAccessTokenUseCase(get()) }
    factory { AuthUseCase(get(), get()) }
    factory { ExtractTokensUseCase() }
    viewModel { LoginViewModel(get(), get()) }
}


