package com.example.spotifyapi.authenticate.data.model

import com.google.gson.annotations.SerializedName


data class SpotifyTokens(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String)

