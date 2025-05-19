package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.domain.mapper.TopArtistMapper.mapToArtistDB
import com.example.spotifyapi.app.domain.mapper.TopArtistMapper.toArtist
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse

class GetTopArtistsUseCase(private val repository: TopArtistsRepository) {

    suspend fun execute(accessToken: String): List<TopArtistInfoResponse> {
        val responseApi = repository.getTopArtists(accessToken)

        if (responseApi.isEmpty()) {
            return getLocalData()
        }

        val success = insertData(responseApi)
        return if (success) responseApi else getLocalData()
    }

    private suspend fun insertData(responseApi: List<TopArtistInfoResponse>): Boolean {
        val (artistsDB, imagesDB) = mapToArtistDB(responseApi)
        repository.insertLocalTopArtists(artistsDB)

        val insertedArtists = repository.getLocalTopArtists()
        if (insertedArtists.isEmpty()) {
            return false
        }

        val validImages = imagesDB.filter { image ->
            insertedArtists.any { it.id == image.artistId }
        }

        repository.insertLocalImages(validImages)
        return true
    }

    private suspend fun getLocalData(): List<TopArtistInfoResponse> {
        val localArtists = repository.getLocalTopArtists()
        val images = repository.getLocalArtistImages()

        return localArtists.map { artist ->
            val filteredImages = images.filter { it.artistId == artist.id }
            artist.toArtist(filteredImages)
        }
    }
}