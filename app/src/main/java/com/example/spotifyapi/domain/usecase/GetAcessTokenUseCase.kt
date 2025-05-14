package com.example.spotifyapi.domain.usecase

import com.example.spotifyapi.data.model.Tokens
import com.example.spotifyapi.data.repository.AuthRepository

class GetAccessTokenUseCase(private val repository: AuthRepository) {
    suspend fun execute(authorizationCode: String, redirectUri: String): Tokens {
        return repository.getAccessToken(authorizationCode, redirectUri)
    }
}


