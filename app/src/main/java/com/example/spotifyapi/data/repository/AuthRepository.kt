package com.example.spotifyapi.data.repository


import com.example.spotifyapi.data.model.SpotifyTokens

interface AuthRepository {
    suspend fun getAccessToken(authorizationCode: String, redirectUri: String): SpotifyTokens

    suspend fun refreshAccessToken(refreshToken: String): SpotifyTokens

    fun saveTokens(accessToken: String, refreshToken: String)

    fun loadTokens(): Pair<String, String>
}

