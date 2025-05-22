package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import retrofit2.Response

class CreatePlaylistRepository(private val apiService: SpotifyApiService,
    private val tokenRepository: TokenRepository
) {

    suspend fun createPlaylist(playlistName: String): Boolean {
        val token = tokenRepository.getAccessToken().orEmpty()
        val requestBody = CreatePlaylistRequest(name = playlistName, public = true)
        val response = apiService.createPlaylist("Bearer $token", requestBody)
        return response.isSuccessful
    }
}