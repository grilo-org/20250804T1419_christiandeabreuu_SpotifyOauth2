package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse

object TopArtistsMapper {

    fun toTopArtistsDB(response: TopArtistsResponse, timeRange: String): TopArtistsDB {
        return TopArtistsDB(
            total = response.total,
            limit = response.limit,
            offset = response.offset,
            href = response.href ?: "",
            next = response.next,
            previous = response.previous,
            timeRange = timeRange
        )
    }

    fun toArtistsDB(response: TopArtistsResponse, topArtistsId: Int): List<ArtistDB> {
        return response.items.map { artist ->
            ArtistDB(
                id = artist.id,
                name = artist.name,
                popularity = artist.popularity,
                topArtistsId = topArtistsId
            )
        }
    }

    fun toImageArtistsDB(response: TopArtistsResponse, artistsIds: List<Long>): List<ImageArtist> {
        return response.items.flatMapIndexed { index, artist ->
            artist.images.map { image ->
                ImageArtist(
                    url = image.url,
                    artistId = artistsIds[index].toInt()
                )
            }
        }
    }
}