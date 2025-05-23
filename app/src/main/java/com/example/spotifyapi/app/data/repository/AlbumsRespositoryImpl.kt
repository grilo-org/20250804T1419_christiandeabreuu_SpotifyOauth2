package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.model.AlbumsResponse
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class AlbumsRepositoryImpl(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) : AlbumsRepository {

    override suspend fun getAlbumsFromApi(artistId: String): AlbumsResponse? {
        return try {
            val token = tokenRepository.getAccessToken().orEmpty()
            apiService.getAlbums(token.addBearer(), artistId)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAlbumsFromDB(artistId: String): List<AlbumDB> {
        return spotifyDAO.getLocalAlbumsByArtist(artistId)
    }

    override suspend fun insertLocalAlbums(albums: List<AlbumDB>) {
        spotifyDAO.insertLocalAlbums(albums)
    }
}