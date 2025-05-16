package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class CreatePlaylistRepository(private val apiService: SpotifyApiService) {

    suspend fun createPlaylist(accessToken: String, playlistName: String): String {
        val requestBody = CreatePlaylistRequest(
            name = playlistName, public = true
        )
        val response = apiService.createPlaylist("Bearer $accessToken", requestBody)
        if (response.isSuccessful) {
            return "Playlist '${playlistName}' criada com sucesso!"
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Erro ao criar playlist: $errorBody")
        }
    }
}