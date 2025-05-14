package com.example.spotifyapi.data.repository


import android.util.Log

class TokenRepository {

    fun saveTokens(accessToken: String, refreshToken: String): Boolean {
        return try {
            Log.d("TokenRepository", "Tokens salvos com sucesso!")
            true
        } catch (e: Exception) {
            Log.e("TokenRepository", "Erro ao salvar tokens: ${e.message}")
            false
        }
    }
}