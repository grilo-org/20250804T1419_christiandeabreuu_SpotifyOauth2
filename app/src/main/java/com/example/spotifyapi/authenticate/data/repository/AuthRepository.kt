package com.example.spotifyapi.authenticate.data.repository

import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.data.networking.AuthApiService

class AuthRepository(private val apiService: AuthApiService) {

    suspend fun getAccessToken(
        authorizationCode: String, redirectUri: String
    ): Result<SpotifyTokens> {
        return try {
            val response = apiService.getAccessToken(
                authorizationCode = authorizationCode,
                redirectUri = redirectUri,
                clientId = BuildConfig.SPOTIFY_CLIENT_ID,
                clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
            )

            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(Exception("Erro: ${response.code()} - ${response.message()}"))
            }

            Result.success(response.body()!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}