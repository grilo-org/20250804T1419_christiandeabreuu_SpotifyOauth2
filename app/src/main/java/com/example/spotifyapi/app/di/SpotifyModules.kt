package com.example.spotifyapi.app.di

import com.example.spotifyapi.app.data.SpotifyAuthHelper
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.oauth2.data.repository.TokenRepository
import com.example.spotifyapi.app.ui.topartists.ArtistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val spotifyModules = module {

    single { TokenRepository() }
    single { SpotifyAuthHelper(get()) }
    viewModel { ArtistViewModel(get())  }
}

val networkModule = module {
    single {
        retrofit2.Retrofit.Builder().baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SpotifyApiService::class.java)
    }
}