package com.example.spotifyapi.auth.data.repository

import com.example.spotifyapi.auth.data.model.SpotifyTokens

interface AuthRepository {

    suspend fun getAccessToken(
        authorizationCode: String, redirectUri: String
    ): Result<SpotifyTokens>
}
