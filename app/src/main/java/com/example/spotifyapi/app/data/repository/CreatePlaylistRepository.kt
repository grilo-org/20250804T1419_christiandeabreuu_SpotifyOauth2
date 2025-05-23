package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class CreatePlaylistRepository(
    private val apiService: SpotifyApiService,
    private val tokenRepository: TokenRepository
) {

    suspend fun createPlaylist(playlistName: String): Boolean {
        val token = tokenRepository.getAccessToken().orEmpty()
        val requestBody = CreatePlaylistRequest(name = playlistName, public = true)
        val response = apiService.createPlaylist(token.addBearer(), requestBody)
        return response.isSuccessful
    }
}