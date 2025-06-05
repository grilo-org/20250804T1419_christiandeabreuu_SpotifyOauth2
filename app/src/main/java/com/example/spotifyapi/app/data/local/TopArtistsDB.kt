package com.example.spotifyapi.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


data class ImageArtist(
    @PrimaryKey(autoGenerate = true) val databaseId: Int = 0, val url: String, val artistId: Int
)

@Entity(tableName = "artist")
data class ArtistDB(
    @PrimaryKey(autoGenerate = true) val databaseId: Int = 0,
    val id: String,
    val name: String,
    val popularity: Int,
    val imageUrl: String,
    val timeRange: String,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val href: String,
    val next: String?,
    val previous: String?
)

data class ArtistIdMap(
    val databaseId: Int, val id: String
)