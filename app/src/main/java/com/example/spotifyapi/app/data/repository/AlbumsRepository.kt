package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.AlbumsResponse

interface AlbumsRepository {
    suspend fun getAlbumsFromApi(artistId: String): AlbumsResponse?
    suspend fun getAlbumsFromDB(artistId: String): List<AlbumDB>
    suspend fun insertLocalAlbums(albums: List<AlbumDB>)
}