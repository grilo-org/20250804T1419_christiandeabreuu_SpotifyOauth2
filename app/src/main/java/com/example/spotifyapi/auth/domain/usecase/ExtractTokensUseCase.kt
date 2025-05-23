package com.example.spotifyapi.auth.domain.usecase

import android.net.Uri
import com.example.spotifyapi.auth.data.model.SpotifyTokens

class ExtractTokensUseCase {
    fun execute(uri: Uri): SpotifyTokens {
        val accessToken = uri.getQueryParameter(ACCESS_TOKEN_KEY).orEmpty()
        val refreshToken = uri.getQueryParameter(REFRESH_TOKEN_KEY).orEmpty()
        return SpotifyTokens(accessToken, refreshToken)
    }
    private companion object
    {
         const val ACCESS_TOKEN_KEY = "access_token"
         const val REFRESH_TOKEN_KEY = "refresh_token"
    }

}