package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse

object TopArtistsMapper {

    fun toArtistsDB(response: TopArtistsResponse, timeRange: String): List<ArtistDB> {
        return response.items.map { artist ->
            ArtistDB(
                id = artist.id,
                name = artist.name,
                popularity = artist.popularity,
                imageUrl = artist.images.firstOrNull()?.url.orEmpty(),
                timeRange = timeRange,
                total = response.total,
                limit = response.limit,
                offset = response.offset,
                href = response.href.orEmpty(),
                next = response.next,
                previous = response.previous
            )
        }
    }
}