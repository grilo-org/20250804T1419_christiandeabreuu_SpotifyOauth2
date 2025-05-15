package com.example.spotifyapi.authenticate.domain.interfacies


import com.example.spotifyapi.authenticate.data.model.SpotifyTokens

interface AuthRepository {
    suspend fun getAccessToken(authorizationCode: String, redirectUri: String): Result<SpotifyTokens>

    suspend fun refreshAccessToken(refreshToken: String): Result<SpotifyTokens>

    fun saveTokens(accessToken: String, refreshToken: String): Boolean

    fun loadTokens(): SpotifyTokens?
}

