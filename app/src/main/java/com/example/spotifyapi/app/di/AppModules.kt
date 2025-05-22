package com.example.spotifyapi.app.di

import androidx.room.Room
import com.example.spotifyapi.app.data.database.SpotifyDatabase
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.ui.album.AlbumsViewModel
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistViewModel
import com.example.spotifyapi.app.ui.playlist.PlaylistViewModel
import com.example.spotifyapi.app.ui.profile.ProfileViewModel
import com.example.spotifyapi.app.ui.topartists.TopArtistsAdapter
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import com.example.spotifyapi.app.ui.topartists.TopArtistsViewModel
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {

    viewModel { TopArtistsViewModel(get(), get(),) }
    viewModel { AlbumsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { PlaylistViewModel(get(), get()) }
    viewModel { CreatePlaylistViewModel(get()) }

    factory { GetAlbumsUseCase(get()) }
    factory { GetUserProfileUseCase(get()) }
    factory { GetPlaylistsUseCase(get()) }
    factory { CreatePlaylistUseCase(get()) }
    factory { GetTopArtistsUseCase(get(), get()) }

    factory { AlbumsRepository(get(), get()) }
    factory { UserProfileRepository(get(), get()) }
    factory { PlaylistRepository(get(), get()) }
    factory { CreatePlaylistRepository(get()) }
    factory { TopArtistsRepository(get(), get()) }

    factory { ArtistPagingSource(get(), get(),) }
    factory { TopArtistsAdapter(get()) }
}

val networkAppModule = module {
    single {
        retrofit2.Retrofit.Builder().baseUrl("https://api.spotify.com/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SpotifyApiService::class.java)
    }
}

val databaseModule = module {

    single {
        Room.databaseBuilder(
            get(), SpotifyDatabase::class.java, "github_database"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<SpotifyDatabase>().spotifyDao() }
}
