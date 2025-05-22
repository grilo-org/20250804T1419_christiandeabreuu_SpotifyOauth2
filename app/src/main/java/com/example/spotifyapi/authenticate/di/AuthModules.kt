package com.example.spotifyapi.authenticate.di

import com.example.spotifyapi.authenticate.data.networking.AuthApiService
import com.example.spotifyapi.authenticate.data.repository.AuthRepository
import com.example.spotifyapi.authenticate.data.repository.TokenRepository
import com.example.spotifyapi.authenticate.domain.usecase.AuthUseCase
import com.example.spotifyapi.authenticate.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.authenticate.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.authenticate.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val authModules = module {
    factory { AuthRepository(apiService = get()) }
    factory { TokenRepository(context = get()) }

    viewModel { LoginViewModel(get(), get()) }

    factory { GetAccessTokenUseCase(get()) }
    factory { AuthUseCase(get(), get()) }
    factory { ExtractTokensUseCase() }

}

val networkAuthModule = module {

    single {
        retrofit2.Retrofit.Builder().baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AuthApiService::class.java)
    }
}




