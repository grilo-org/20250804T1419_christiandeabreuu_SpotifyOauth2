package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Playlist

interface PlaylistRepository {
    suspend fun getPlaylistsFromApi(): List<Playlist>
    suspend fun insertPlaylistsIntoDB(playlists: List<PlaylistDB>)
    suspend fun getPlaylistsFromDB(): List<PlaylistDB>
}