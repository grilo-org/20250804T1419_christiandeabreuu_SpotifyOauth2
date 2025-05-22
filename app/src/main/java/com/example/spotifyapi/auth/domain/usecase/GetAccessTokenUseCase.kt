package com.example.spotifyapi.auth.domain.usecase

import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.repository.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): Result<SpotifyTokens> {
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}


