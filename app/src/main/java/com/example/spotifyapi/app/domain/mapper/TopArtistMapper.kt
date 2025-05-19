package com.example.spotifyapi.app.domain.mapper


import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtistDB
import com.example.spotifyapi.app.data.model.ImageArtist
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse

object TopArtistMapper {

    fun ArtistDB.toArtist(images: List<ImageArtistDB>): TopArtistInfoResponse {
        return TopArtistInfoResponse(
            id = this.id,
            name = this.name,
            popularity = this.popularity,
            images = images.map { ImageArtist(url = it.url) }
        )
    }

    fun mapToArtistDB(artists: List<TopArtistInfoResponse>): Pair<List<ArtistDB>, List<ImageArtistDB>> {
        val artistDBList = artists.map { artistResponse ->
            ArtistDB(
                id = artistResponse.id,
                name = artistResponse.name,
                popularity = artistResponse.popularity,
                topArtistsId = artistResponse.id.hashCode()
            )
        }

        val imageArtistList = artists.flatMap { artistResponse ->
            artistResponse.images.map { imageUrl ->
                ImageArtistDB(url = imageUrl.url, artistId = artistResponse.id)
            }
        }

        return artistDBList to imageArtistList
    }
}