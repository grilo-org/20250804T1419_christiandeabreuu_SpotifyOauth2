package com.example.spotifyapi.app.domain.usecase

import android.util.Log
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtistDB
import com.example.spotifyapi.app.data.model.ImageArtist
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse
import com.example.spotifyapi.app.data.repository.TopArtistsRepository

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

    private fun mapToArtistDB(artists: List<TopArtistInfoResponse>): Pair<List<ArtistDB>, List<ImageArtistDB>> {
        val artistDBList = artists.map { artistResponse ->
            ArtistDB(
                id = artistResponse.id,
                name = artistResponse.name,
                popularity = artistResponse.popularity,
                topArtistsId = artistResponse.id.hashCode()
            )
        }

        val imageArtistList = artists.flatMap { artistResponse ->
            artistResponse.images.map { imageUrl ->
                ImageArtistDB(
                    url = imageUrl.url, artistId = artistResponse.id
                )
            }
        }

        return artistDBList to imageArtistList
    }

    private fun ArtistDB.toArtist(images: List<ImageArtistDB>): TopArtistInfoResponse {
        return TopArtistInfoResponse(id = this.id,
            name = this.name,
            popularity = this.popularity,
            images = images.map { ImageArtist(url = it.url) })
    }
}