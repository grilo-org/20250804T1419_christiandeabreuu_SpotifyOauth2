package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class PlaylistRepositoryImpl(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) : PlaylistRepository {
    override suspend fun getPlaylistsFromApi(): List<Playlist> {
        return try {
            val token = tokenRepository.getAccessToken()
            val response = apiService.getPlaylists(token.addBearer())
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun insertPlaylistsIntoDB(playlists: List<PlaylistDB>) {
        spotifyDAO.insertLocalPlaylists(playlists)
    }

    override suspend fun getPlaylistsFromDB(): List<PlaylistDB> {
        val playlists = spotifyDAO.getLocalPlaylists()
        return playlists
    }
}