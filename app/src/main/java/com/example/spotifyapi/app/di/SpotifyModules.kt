package com.example.spotifyapi.app.di

import com.example.spotifyapi.app.data.SpotifyAuthHelper
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileTopArtistsUseCase
import com.example.spotifyapi.authenticate.data.repository.TokenRepository
import com.example.spotifyapi.app.ui.topartists.TopArtistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val spotifyModules = module {

    single { TokenRepository(context = get()) }
    single { SpotifyAuthHelper(get()) }
    viewModel { TopArtistsViewModel(get(), get(), get())  }

    factory { GetTopArtistsUseCase(get()) }
    factory { GetUserProfileTopArtistsUseCase(get()) }
    factory { TopArtistsRepository(get()) }
    factory { UserProfileRepository(get()) }
}

val networkModule = module {
    single {
        retrofit2.Retrofit.Builder().baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SpotifyApiService::class.java)
    }
}