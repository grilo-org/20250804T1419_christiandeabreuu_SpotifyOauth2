package com.example.spotifyapi.oauth2.domain.interf


import com.example.spotifyapi.oauth2.data.model.SpotifyTokens

interface AuthRepository {
    suspend fun getAccessToken(authorizationCode: String, redirectUri: String): SpotifyTokens

    suspend fun refreshAccessToken(refreshToken: String): SpotifyTokens

    fun saveTokens(accessToken: String, refreshToken: String)

    fun loadTokens(): Pair<String, String>
}

