package com.example.spotifyapi.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SpotifyDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertArtists(artists: List<ArtistDB>) // 🔹 Salva artistas no banco
//
//    @Query("SELECT * FROM artist")
//    suspend fun getAllArtists(): List<ArtistDB> // 🔹 Busca artistas do banco

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileDB): Long

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserProfileDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistDB>)

    @Query("SELECT * FROM playlist")
    suspend fun getPlaylists(): List<PlaylistDB>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAlbums(albums: List<AlbumDB>)
//
//    @Query("SELECT * FROM albums")
//    suspend fun getAlbums(): List<AlbumDB>
//
//    @Query("SELECT * FROM albums WHERE databaseId = :albumId LIMIT 1")
//    suspend fun getAlbumById(albumId: String): AlbumDB?
}