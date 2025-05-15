package com.example.spotifyapi.authenticate.data.repository


import android.content.Context
import android.util.Log
import com.example.spotifyapi.authenticate.data.model.SpotifyTokens

class TokenRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("SpotifyPrefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return try {
            val success = sharedPreferences.edit().apply {
                putString("ACCESS_TOKEN", accessToken)
                putString("REFRESH_TOKEN", refreshToken)
            }.commit()

            Log.d("TokenRepository", "Tokens salvos com sucesso!")
            success
        } catch (e: Exception) {
            Log.e("TokenRepository", "Erro ao salvar tokens: ${e.message}")
            false
        }
    }

    fun getTokens(): SpotifyTokens? {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)
        return if (accessToken != null && refreshToken != null) {
            SpotifyTokens(accessToken, refreshToken)
        } else {
            null
        }
    }
}

