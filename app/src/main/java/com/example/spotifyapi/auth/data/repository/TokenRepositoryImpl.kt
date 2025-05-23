package com.example.spotifyapi.auth.data.repository

import android.content.Context

class TokenRepositoryImpl(context: Context) : TokenRepository {
    private val sharedPreferences =
        context.getSharedPreferences(SPOTIFY_PREFS, Context.MODE_PRIVATE)

    override fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return try {
            val success = sharedPreferences.edit().apply {
                putString(ACCESS_TOKEN_KEY, accessToken)
                putString(REFRESH_TOKEN_KEY, refreshToken)
            }.commit()
            success
        } catch (e: Exception) {
            false
        }
    }

    override fun getAccessToken(): String? =
        sharedPreferences.getString(ACCESS_TOKEN_KEY, null)

    private companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
        private const val SPOTIFY_PREFS = "SpotifyPrefs"
    }
}