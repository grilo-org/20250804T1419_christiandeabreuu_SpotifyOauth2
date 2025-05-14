package com.example.spotifyapi.domain.usecase

import com.example.spotifyapi.data.model.SpotifyTokens
import com.example.spotifyapi.data.repository.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): SpotifyTokens {
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}


