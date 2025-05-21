package com.example.spotifyapi.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileDB(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int = 0,
    val id: String,
    val name: String,
    val imageUrl: String?,
)
