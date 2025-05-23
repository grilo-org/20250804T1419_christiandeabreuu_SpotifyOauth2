package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.data.model.Playlist

object PlaylistMapper {

    fun Playlist.toPlaylistDB(): PlaylistDB {
        return PlaylistDB(
            id = this.id,
            name = this.name,
            description = this.description,
            ownerName = this.owner.name,
            tracksCount = this.tracksCount,
            imageUrl = this.images?.firstOrNull()?.url.orEmpty()
        )
    }

    fun PlaylistDB.toPlaylist(): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            owner = Owner(id = "", name = this.ownerName),
            tracksCount = this.tracksCount,
            images = this.imageUrl?.takeIf { it.isNotBlank() }
                ?.let { listOf(Image(url = it)) }
                ?: emptyList()
        )
    }
}