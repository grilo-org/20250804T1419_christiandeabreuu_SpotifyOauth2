package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.model.AlbumsResponse
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class AlbumsRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) {

    suspend fun getAlbumsFromApi(artistId: String): AlbumsResponse? {
        return try {
            val token = tokenRepository.getAccessToken().orEmpty()
            apiService.getAlbums(token.addBearer(), artistId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAlbumsFromDB(artistId: String): List<AlbumDB> {
        return spotifyDAO.getLocalAlbumsByArtist(artistId)
    }

    suspend fun insertLocalAlbums(albums: List<AlbumDB>) {
        spotifyDAO.insertLocalAlbums(albums)
    }
}