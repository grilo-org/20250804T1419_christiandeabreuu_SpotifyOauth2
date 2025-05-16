package com.example.spotifyapi.app.di

import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfilePlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistViewModel
import com.example.spotifyapi.app.ui.playlist.PlaylistViewModel
import com.example.spotifyapi.app.ui.profile.ProfileViewModel
import com.example.spotifyapi.app.ui.topartists.TopArtistsViewModel
import com.example.spotifyapi.authenticate.data.networking.AuthApiService
import com.example.spotifyapi.authenticate.data.repository.TokenRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val spotifyModules = module {

    single { TokenRepository(context = get()) }

    viewModel { TopArtistsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { PlaylistViewModel(get(), get()) }
    viewModel { CreatePlaylistViewModel(get()) }

    factory { GetTopArtistsUseCase(get()) }
    factory { GetUserProfileTopArtistsUseCase(get()) }
    factory { GetUserProfileUseCase(get()) }
    factory { GetUserProfilePlaylistsUseCase(get()) }
    factory { GetPlaylistsUseCase(get()) }
    factory { CreatePlaylistUseCase(get()) }

    factory { TopArtistsRepository(get()) }
    factory { UserProfileRepository(get()) }
    factory { PlaylistRepository(get()) }
    factory { CreatePlaylistRepository(get()) }

}

val networkModule = module {
    single {
        retrofit2.Retrofit.Builder().baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SpotifyApiService::class.java)
    }
    single {
        retrofit2.Retrofit.Builder().baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AuthApiService::class.java)
    }
}
