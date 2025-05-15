package com.example.spotifyapi.oauth2.domain.usecase

import com.example.spotifyapi.oauth2.data.model.SpotifyTokens
import com.example.spotifyapi.oauth2.domain.interfacies.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): Result<SpotifyTokens> {
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}


