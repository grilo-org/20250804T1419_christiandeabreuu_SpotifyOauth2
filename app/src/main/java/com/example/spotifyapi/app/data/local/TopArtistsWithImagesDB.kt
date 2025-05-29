package com.example.spotifyapi.app.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class TopArtistsWithArtistsAndImages(
    @Embedded val topArtists: TopArtistsDB,
    @Relation(
        entity = ArtistDB::class,
        parentColumn = "databaseId",
        entityColumn = "topArtistsId"
    )
    val artists: List<ArtistWithImages>
)

data class ArtistWithImages(
    @Embedded val artist: ArtistDB,
    @Relation(
        parentColumn = "databaseId",
        entityColumn = "artistId"
    )
    val images: List<ImageArtist>
)

