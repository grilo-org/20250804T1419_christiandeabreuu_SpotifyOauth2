package com.example.spotifyapi.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.local.UserProfileDB

@Dao
interface SpotifyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistDB>): List<Long>

    @Transaction
    @Query("SELECT * FROM artist WHERE timeRange = :timeRange LIMIT :limit OFFSET :offset")
    suspend fun getTopArtistsWithOffsetAndLimit(
        limit: Int, offset: Int, timeRange: String
    ): List<ArtistDB>

    @Query("SELECT * FROM albums WHERE artistId = :artistId")
    suspend fun getLocalAlbumsByArtist(artistId: String): List<AlbumDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalAlbums(albums: List<AlbumDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalUserProfile(userProfile: UserProfileDB): Long

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getLocalUserProfile(): UserProfileDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalPlaylists(playlists: List<PlaylistDB>)

    @Query("SELECT * FROM playlist")
    suspend fun getLocalPlaylists(): List<PlaylistDB>
}