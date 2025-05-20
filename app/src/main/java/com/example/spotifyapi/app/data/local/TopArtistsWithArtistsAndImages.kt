//package com.example.spotifyapi.app.data.local
//
//import androidx.room.Embedded
//import androidx.room.Relation
//import com.example.spotifyapi.app.data.model.Artist
//
//data class TopArtistsWithArtistsAndImages(
//    @Embedded val topArtists: TopArtistsDB,
//    @Relation(
//        entity = ArtistDB::class,
//        parentColumn = "databaseId",
//        entityColumn = "topArtistsId"
//    )
//    val artists: List<ArtistWithImages>
//)
//
//data class ArtistWithImages(
//    @Embedded val artist: Artist,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "artistId"
//    )
//    val images: List<ImageArtistDB>
//)
//
//
//
//
