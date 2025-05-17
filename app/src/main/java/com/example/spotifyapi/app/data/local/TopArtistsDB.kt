package com.example.spotifyapi.app.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

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

@Entity(tableName = "artist")
data class ArtistDB( // Agora fica claro que esta versão representa o banco de dados
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val id: String, // ID da API
    val name: String,
    val popularity: Int,
    val topArtistsId: Int // Relaciona com a tabela TopArtistsDB
)

@Entity(
    tableName = "image_artist",
    foreignKeys = [ForeignKey(
        entity = ArtistDB::class,
        parentColumns = ["databaseId"],
        childColumns = ["artistId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ImageArtist(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val url: String,
    val artistId: Int
)

data class ArtistWithImages(
    @Embedded val artist: ArtistDB,
    @Relation(parentColumn = "databaseId", entityColumn = "artistId")
    val images: List<ImageArtist>
)