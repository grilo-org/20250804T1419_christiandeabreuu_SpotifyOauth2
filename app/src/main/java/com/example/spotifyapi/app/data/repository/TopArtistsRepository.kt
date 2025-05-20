//package com.example.spotifyapi.app.data.repository
//
//import com.example.spotifyapi.app.data.local.ArtistDB
//import com.example.spotifyapi.app.data.local.ImageArtistDB
//import com.example.spotifyapi.app.data.local.SpotifyDAO
//import com.example.spotifyapi.app.data.model.ArtistResponse
//import com.example.spotifyapi.app.data.networking.SpotifyApiService
//
//class TopArtistsRepository(
//    private val spotifyDAO: SpotifyDAO,
//    private val apiService: SpotifyApiService
//) {
//    suspend fun getTopArtists(accessToken: String, offset: Int = 0, timeRange: String = "medium_term"): List<ArtistResponse> {
//        return try {
//            val response = apiService.getTopArtists("Bearer $accessToken", offset)
//            response.items
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//
//    suspend fun insertLocalTopArtists(artists: List<ArtistDB>) {
//        spotifyDAO.insertLocalTopArtistsInfo(artists)
//    }
//
//    suspend fun getLocalTopArtists(): List<ArtistDB> {
//        return spotifyDAO.getLocalTopArtistsInfo()
//    }
//
//    suspend fun insertLocalImages(images: List<ImageArtistDB>) {
//        spotifyDAO.insertLocalTopArtistsImages(images)
//    }
//
//    suspend fun getLocalArtistImages(): List<ImageArtistDB> {
//        return spotifyDAO.getLocalTopArtistImages()
//    }
//}