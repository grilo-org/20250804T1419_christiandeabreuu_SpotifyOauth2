package com.example.spotifyapi.auth.domain.usecase

import android.net.Uri
import com.example.spotifyapi.auth.data.model.SpotifyTokens

class ExtractTokensUseCase {
    fun execute(uri: Uri): SpotifyTokens {
        val accessToken = uri.getQueryParameter("access_token") ?: ""
        val refreshToken = uri.getQueryParameter("refresh_token") ?: ""
        return SpotifyTokens(accessToken, refreshToken)
    }
}