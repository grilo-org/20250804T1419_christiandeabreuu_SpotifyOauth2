package com.example.spotifyapi.app.data.repository

interface CreatePlaylistRepository {
    suspend fun createPlaylist(playlistName: String): Boolean
}