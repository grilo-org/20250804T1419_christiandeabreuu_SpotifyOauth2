package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.model.ImageArtist
import com.example.spotifyapi.app.data.repository.AlbumsRepository

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

    private fun Album.toAlbumDB(artistId: String): AlbumDB {
        return AlbumDB(
            databaseId = this.id,
            name = this.name,
            artistId = artistId,
            imageUrl = this.images.firstOrNull()?.url,
            releaseDate = this.releaseDate
        )
    }

    private fun AlbumDB.toAlbum(): Album {
        return Album(
            id = this.databaseId,
            name = this.name,
            releaseDate = this.releaseDate,
            images = listOf(ImageArtist(this.imageUrl ?: "")),
            artistId = this.artistId
        )
    }
}