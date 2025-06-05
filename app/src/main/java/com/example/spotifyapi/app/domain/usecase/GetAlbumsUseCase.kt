package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbumDB

class GetAlbumsUseCase(private val repository: AlbumsRepository) {

    suspend fun loadAlbums(artistId: String): List<Album> {
        val albumsDB = getAlbumsFromLocal(artistId)
        val albumsFromApi = fetchAlbumsFromApi(artistId)

        return if (albumsFromApi.isNotEmpty() && albumsFromApi != albumsDB) {
            repository.insertLocalAlbums(albumsFromApi)
            albumsFromApi.map { it.toAlbum() }
        } else {
            albumsDB.map { it.toAlbum() }
        }
    }

    private suspend fun getAlbumsFromLocal(artistId: String): List<AlbumDB> {
        return repository.getAlbumsFromDB(artistId)
    }

    private suspend fun fetchAndCacheAlbums(artistId: String): List<Album> {
        val albumsFromApi = fetchAlbumsFromApi(artistId)
        repository.insertLocalAlbums(albumsFromApi)

        return albumsFromApi.map { it.toAlbum() }
    }

    private suspend fun fetchAlbumsFromApi(artistId: String): List<AlbumDB> {
        val response = repository.getAlbumsFromApi(artistId)
        return response?.items?.map { it.toAlbumDB(artistId) } ?: emptyList()
    }
}