//package com.example.spotifyapi.app.domain.usecase
//
//import android.util.Log
//import com.example.spotifyapi.app.data.model.ArtistResponse
//import com.example.spotifyapi.app.data.repository.TopArtistsRepository
//import com.example.spotifyapi.app.domain.mapper.TopArtistMapper.mapToArtistDB
//import com.example.spotifyapi.app.domain.mapper.TopArtistMapper.toArtist
//
//class GetTopArtistsUseCase(private val repository: TopArtistsRepository) {
//
//    suspend fun getTopArtisFromApi(accessToken: String, offset: Int = 0,  timeRange: String = "medium_term"): List<ArtistResponse> {
//        val responseApi = repository.getTopArtists(accessToken, offset, timeRange)
//
//        Log.d("UseCase", "🔄 Artistas recebidos: ${responseApi.map { it.name }}")
//
//        return if (responseApi.isEmpty()) getLocalData() else responseApi
//    }
//
//    private suspend fun insertData(responseApi: List<ArtistResponse>): Boolean {
//        val (artistsDB, imagesDB) = mapToArtistDB(responseApi)
//        repository.insertLocalTopArtists(artistsDB)
//
//        val insertedArtists = repository.getLocalTopArtists()
//        if (insertedArtists.isEmpty()) {
//            return false
//        }
//
//        val validImages = imagesDB.filter { image ->
//            insertedArtists.any { it.id == image.artistId }
//        }
//
//        repository.insertLocalImages(validImages)
//        return true
//    }
//
//    private suspend fun getLocalData(): List<ArtistResponse> {
//        val localArtists = repository.getLocalTopArtists()
//        val images = repository.getLocalArtistImages()
//
//        return localArtists.map { artist ->
//            val filteredImages = images.filter { it.artistId == artist.id }
//            artist.toArtist(filteredImages)
//        }
//    }
//}