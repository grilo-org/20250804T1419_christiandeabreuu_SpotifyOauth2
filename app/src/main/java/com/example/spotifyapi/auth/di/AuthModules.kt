package com.example.spotifyapi.auth.di

import com.example.spotifyapi.auth.data.networking.AuthApiService
import com.example.spotifyapi.auth.data.repository.AuthRepository
import com.example.spotifyapi.auth.data.repository.AuthRepositoryImpl
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.auth.data.repository.TokenRepositoryImpl
import com.example.spotifyapi.auth.domain.usecase.AuthUseCase
import com.example.spotifyapi.auth.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.auth.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.auth.ui.login.LoginViewModel
import com.example.spotifyapi.utils.Constants.BASE_URL_AUTH
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val authModules = module {
    factory<AuthRepository> { AuthRepositoryImpl(apiService = get(), resourcesPlugin = get()) }
    factory<TokenRepository> { TokenRepositoryImpl(context = get()) }

    viewModel { LoginViewModel(get(), get(), get()) }

    factory { GetAccessTokenUseCase(get()) }
    factory { AuthUseCase(get(), get(), get()) }
    factory { ExtractTokensUseCase() }
}

val networkAuthModule = module {

    single {
        retrofit2.Retrofit.Builder().baseUrl(BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AuthApiService::class.java)
    }
}




