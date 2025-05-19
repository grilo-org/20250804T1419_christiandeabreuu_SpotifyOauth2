package com.example.spotifyapi.app.data.networking

import com.example.spotifyapi.app.data.model.AlbumsResponse
import com.example.spotifyapi.app.data.model.CreatePlaylistRequest
import com.example.spotifyapi.app.data.model.PlaylistsResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("artists/{id}/albums")
    suspend fun getAlbums(
        @Header("Authorization") authorization: String,
        @Path("id") artistId: String
    ): AlbumsResponse

    @GET("me/playlists")
    suspend fun getPlaylists(@Header("Authorization") authorization: String): PlaylistsResponse

    @POST("me/playlists")
    suspend fun createPlaylist(
        @Header("Authorization") accessToken: String, @Body requestBody: CreatePlaylistRequest
    ): Response<Unit>

}


