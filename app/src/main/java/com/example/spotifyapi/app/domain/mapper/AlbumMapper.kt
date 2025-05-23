package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.local.ImageArtist

object AlbumMapper {

    fun Album.toAlbumDB(artistId: String): AlbumDB {
        return AlbumDB(
            databaseId = this.id,
            name = this.name,
            artistId = artistId,
            imageUrl = this.images.firstOrNull()?.url,
            releaseDate = this.releaseDate
        )
    }

    fun AlbumDB.toAlbum(): Album {
        return Album(
            id = this.databaseId,
            name = this.name,
            artistId = this.artistId,
            releaseDate = this.releaseDate,
            images = listOf(ImageArtist(url = this.imageUrl.orEmpty(), artistId = 0)),
        )
    }
}

