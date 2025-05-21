package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
//import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbumDB

class GetAlbumsUseCase(private val repository: AlbumsRepository) {

    suspend fun execute(accessToken: String, artistId: String): List<Album> {
        val albumsDB = getAlbumsFromDB(artistId)

        return if (albumsDB.isEmpty()) {
            fetchAndStoreAlbums(accessToken, artistId)
        } else {
            albumsDB.map { it.toAlbum() }
        }
    }

    private suspend fun getAlbumsFromDB(artistId: String): List<AlbumDB> {
        return repository.getAlbumsFromDB(artistId)
    }

    private suspend fun fetchAndStoreAlbums(accessToken: String, artistId: String): List<Album> {
        val albumsFromApi = fetchAlbumsFromApi(accessToken, artistId)
        repository.insertLocalAlbums(albumsFromApi)

        return albumsFromApi.map { it.toAlbum() }
    }

    private suspend fun fetchAlbumsFromApi(accessToken: String, artistId: String): List<AlbumDB> {
        val response = repository.getAlbumsFromApi(accessToken, artistId)
        return response?.items?.map { it.toAlbumDB(artistId) } ?: emptyList()
    }
}