package com.example.spotifyapi.app.data.networking

import com.example.spotifyapi.app.data.model.PlaylistsResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.model.UserProfile
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SpotifyApiService {

    @GET("me")
    suspend fun getUserProfile(@Header("Authorization") authorization: String): UserProfile

    @GET("me/top/artists")
    suspend fun getTopArtists(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int = 20,
        @Query("time_range") timeRange: String = "medium_term"
    ): TopArtistsResponse

    @GET("me/playlists")
    suspend fun getPlaylists(@Header("Authorization") authorization: String): PlaylistsResponse
}


