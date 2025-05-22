package com.example.spotifyapi.auth.di

import com.example.spotifyapi.auth.data.networking.AuthApiService
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import com.example.spotifyapi.auth.data.plugin.ResourcesPluginImpl
import com.example.spotifyapi.auth.data.repository.AuthRepository
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.auth.domain.usecase.AuthUseCase
import com.example.spotifyapi.auth.domain.usecase.ExtractTokensUseCase
import com.example.spotifyapi.auth.domain.usecase.GetAccessTokenUseCase
import com.example.spotifyapi.auth.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val authModules = module {
    factory { AuthRepository(apiService = get(), resourcesPlugin = get()) }
    factory { TokenRepository(context = get()) }

    viewModel { LoginViewModel(get(), get()) }

    factory { GetAccessTokenUseCase(get()) }
    factory { AuthUseCase(get(), get()) }
    factory { ExtractTokensUseCase() }
    factory<ResourcesPlugin> { ResourcesPluginImpl(get()) }

}

val networkAuthModule = module {

    single {
        retrofit2.Retrofit.Builder().baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AuthApiService::class.java)
    }
}




