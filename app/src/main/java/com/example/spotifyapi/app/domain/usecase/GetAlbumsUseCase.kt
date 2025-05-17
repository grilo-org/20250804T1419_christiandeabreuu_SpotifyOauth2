package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository

class GetAlbumsUseCase(private val repository: AlbumsRepository) {
    suspend fun execute(accessToken: String, artistId: String): List<Album> {
        return repository.getAlbums(accessToken, artistId)
    }
}