package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.repository.PlaylistRepository

class GetPlaylistsUseCase(private val repository: PlaylistRepository)  {
    suspend fun execute(accessToken: String): List<Playlist> {
        return repository.getPlaylists(accessToken)
    }
}