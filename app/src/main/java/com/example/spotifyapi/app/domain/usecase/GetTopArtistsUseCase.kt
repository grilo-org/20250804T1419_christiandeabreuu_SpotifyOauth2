package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.data.repository.TopArtistsRepository

class GetTopArtistsUseCase(private val repository: TopArtistsRepository) {
    suspend fun execute(accessToken: String): List<Artist> {
        return repository.getTopArtists(accessToken)
    }
}