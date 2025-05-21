package com.example.spotifyapi.authenticate.data.repository

import android.content.Context

class TokenRepository(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return try {
            val success = sharedPreferences.edit().apply {
                putString("ACCESS_TOKEN", accessToken)
                putString("REFRESH_TOKEN", refreshToken)
            }.commit()
            success
        } catch (e: Exception) {
            false
        }
    }
}

