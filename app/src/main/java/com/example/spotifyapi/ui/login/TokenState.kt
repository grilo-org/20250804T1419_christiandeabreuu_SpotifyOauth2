package com.example.spotifyapi.ui.login

import com.example.spotifyapi.data.model.Tokens

data class TokenState(val token: Tokens? = null, val event: TokenStateEvent = TokenStateEvent.Loading)

sealed interface TokenStateEvent {
    data object GetToken : TokenStateEvent
    data object Loading : TokenStateEvent
}