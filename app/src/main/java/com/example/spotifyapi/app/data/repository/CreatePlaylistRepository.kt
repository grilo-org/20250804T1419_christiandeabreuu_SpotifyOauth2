package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import retrofit2.Response

class CreatePlaylistRepository(private val apiService: SpotifyApiService) {

    suspend fun createPlaylist(accessToken: String, playlistName: String): Boolean {
        val requestBody = CreatePlaylistRequest(name = playlistName, public = true)
        val response = apiService.createPlaylist("Bearer $accessToken", requestBody)
        return response.isSuccessful
    }
}