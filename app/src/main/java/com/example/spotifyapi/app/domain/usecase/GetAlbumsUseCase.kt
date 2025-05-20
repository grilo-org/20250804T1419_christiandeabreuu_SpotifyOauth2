package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
//import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbumDB

class GetAlbumsUseCase(private val repository: AlbumsRepository) {

    suspend fun execute(accessToken: String, artistId: String): List<Album> {
        val albumsDB = repository.getAlbumsFromDB(artistId).ifEmpty {
            val response = repository.getAlbumsFromApi(accessToken, artistId)
            val albumsFromApi = response?.items?.map { it.toAlbumDB(artistId) } ?: emptyList()
            repository.insertLocalAlbums(albumsFromApi)
            albumsFromApi
        }

        return albumsDB.map { it.toAlbum() }
    }
}