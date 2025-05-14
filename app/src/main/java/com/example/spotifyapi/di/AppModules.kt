package com.example.spotifyapi.di

import com.example.spotifyapi.data.networking.SpotifyApiService
import com.example.spotifyapi.data.repository.AuthRepository
import com.example.spotifyapi.data.repository.AuthRepositoryImpl
import com.example.spotifyapi.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(get()) }

    factory { GetAccessTokenUseCase(get()) }

    viewModel { LoginViewModel(get()) }
}

val networkModule = module {
    single {
        retrofit2.Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SpotifyApiService::class.java)
    }
}
