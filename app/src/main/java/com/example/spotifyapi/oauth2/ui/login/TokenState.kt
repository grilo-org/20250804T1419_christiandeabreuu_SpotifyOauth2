package com.example.spotifyapi.oauth2.ui.login

import com.example.spotifyapi.oauth2.data.model.SpotifyTokens

data class TokenState(
    val token: SpotifyTokens? = null,
    val event: TokenStateEvent = TokenStateEvent.Loading
)

sealed interface TokenStateEvent {
    data object GetToken : TokenStateEvent
    data object Loading : TokenStateEvent
}