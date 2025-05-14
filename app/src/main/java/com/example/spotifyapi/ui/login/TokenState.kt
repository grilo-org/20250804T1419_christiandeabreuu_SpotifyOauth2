package com.example.spotifyapi.ui.login

import com.example.spotifyapi.data.model.SpotifyTokens

data class TokenState(
    val token: SpotifyTokens? = null,
    val event: TokenStateEvent = TokenStateEvent.Loading
)

sealed interface TokenStateEvent {
    data object GetToken : TokenStateEvent
    data object Loading : TokenStateEvent
}