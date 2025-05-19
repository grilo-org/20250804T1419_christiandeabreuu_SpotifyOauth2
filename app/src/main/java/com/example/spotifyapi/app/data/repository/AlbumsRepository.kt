package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.model.AlbumsResponse

class AlbumsRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO
) {
    suspend fun getAlbumsFromDB(artistId: String): List<AlbumDB> {
        return spotifyDAO.getLocalAlbumsByArtist(artistId)
    }

    suspend fun getAlbumsFromApi(accessToken: String, artistId: String): AlbumsResponse? {
        return try {
            apiService.getAlbums("Bearer $accessToken", artistId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertLocalAlbums(albums: List<AlbumDB>) {
        spotifyDAO.insertLocalAlbums(albums)
    }
}