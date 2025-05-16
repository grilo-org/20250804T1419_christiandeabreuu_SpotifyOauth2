package com.example.spotifyapi.authenticate.data.repository

import android.content.Context
import com.example.spotifyapi.BuildConfig
import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.data.networking.AuthApiService
import com.example.spotifyapi.authenticate.domain.interfacies.AuthRepository
import com.example.spotifyapi.utils.Constants.SHAREDPREFS

class AuthRepositoryImpl(context: Context, private val apiService: AuthApiService) :
    AuthRepository {

    private val sharedPreferences = context.getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE)

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
                return Result.failure(Exception("Erro: ${response.code()} - ${response.message()}"))
            }

            Result.success(response.body()!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshAccessToken(refreshToken: String): Result<SpotifyTokens> {
        return try {
            val response = apiService.refreshAccessToken(
                refreshToken = refreshToken,
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

    override fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return sharedPreferences.edit().apply {
            putString("ACCESS_TOKEN", accessToken)
            putString("REFRESH_TOKEN", refreshToken)
        }.commit()
    }

    override fun loadTokens(): SpotifyTokens? {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)

        return if (accessToken != null && refreshToken != null) {
            SpotifyTokens(accessToken, refreshToken)
        } else {
            null
        }
    }
}