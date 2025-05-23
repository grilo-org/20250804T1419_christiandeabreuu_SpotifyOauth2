package com.example.spotifyapi.auth.data.repository

interface TokenRepository {
    fun saveTokens(accessToken: String, refreshToken: String): Boolean
    fun getAccessToken(): String?
}