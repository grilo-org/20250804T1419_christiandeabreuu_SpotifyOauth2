package com.example.spotifyapi.authenticate.di

import androidx.room.Room
import com.example.spotifyapi.app.data.local.SpotifyDatabase
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.authenticate.data.networking.AuthApiService
import com.example.spotifyapi.authenticate.domain.interfacies.AuthRepository
import com.example.spotifyapi.authenticate.data.repository.AuthRepositoryImpl
import com.example.spotifyapi.authenticate.data.repository.TokenRepository
import com.example.spotifyapi.authenticate.domain.usecase.AuthUseCase
import com.example.spotifyapi.authenticate.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.authenticate.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.authenticate.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory


val authModules = module {
    single { TokenRepository(context = get()) }

    viewModel { LoginViewModel(get(), get()) }

    factory { GetAccessTokenUseCase(get()) }
    factory { AuthUseCase(get(), get()) }
    factory { ExtractTokensUseCase() }

    factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}

val networkAuthModule = module {
    
    single {
        retrofit2.Retrofit.Builder().baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AuthApiService::class.java)
    }
}




