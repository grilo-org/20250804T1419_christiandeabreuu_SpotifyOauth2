package com.example.spotifyapi.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "top_artists")
data class TopArtistsDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val total: Int,
    val limit: Int,
    val offset: Int,
    val href: String,
    val next: String?,
    val previous: String?,
    val timeRange: String
)



@Entity(
    tableName = "artist",
    indices = [Index(value = ["id"], unique = true)] // 🔹 Torna 'id' único
)
data class ArtistDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0, // Chave primária autogerada

    val id: String, // Agora será garantido que 'id' seja único
    val name: String,
    val popularity: Int,
    val topArtistsId: Int
)

@Entity(
    tableName = "image_artist",
    foreignKeys = [ForeignKey(
        entity = ArtistDB::class,
        parentColumns = ["id"],  // Alterado para "id" ao invés de "databaseId"
        childColumns = ["artistId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ImageArtistDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val url: String,
    val artistId: String  // Alterado de Int para String
)
