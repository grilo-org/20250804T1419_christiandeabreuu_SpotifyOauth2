package com.example.spotifyapi.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumDB(
    @PrimaryKey val databaseId: String,
    val name: String,
    val artistId: String,
    val imageUrl: String?,
    val releaseDate: String
)