package com.example.spotifyapi.authenticate.domain.usecase

import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.domain.interfacies.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): Result<SpotifyTokens> {
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}


