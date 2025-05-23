package com.example.spotifyapi.auth.data.repository

import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.networking.AuthApiService
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val resourcesPlugin: ResourcesPlugin
): AuthRepository {

    override suspend fun getAccessToken(
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
                return Result.failure(Exception(resourcesPlugin.getRequestTokenErrorMessage(response.code().toString(), response.message())))
            }
            Result.success(response.body()!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}