package com.example.spotify.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spotifyapi.app.ui.topartists.b.ArtistDB
import com.example.spotifyapi.app.ui.topartists.b.ImageArtist
import com.example.spotifyapi.app.ui.topartists.b.TopArtistsDB

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




